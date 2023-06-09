package com.jayce.seckillsystem.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import com.jayce.seckillsystem.constant.RedisConstant;
import com.jayce.seckillsystem.constant.ResultEnum;
import com.jayce.seckillsystem.entity.GoodsStore;
import com.jayce.seckillsystem.entity.SkMessage;
import com.jayce.seckillsystem.entity.User;
import com.jayce.seckillsystem.entity.resp.Result;
import com.jayce.seckillsystem.entity.vo.GoodsVo;
import com.jayce.seckillsystem.rabbitmq.SkMessageSender;
import com.jayce.seckillsystem.service.IAccessRuleService;
import com.jayce.seckillsystem.service.IGoodsService;
import com.jayce.seckillsystem.service.ISkOrderService;
import com.jayce.seckillsystem.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 秒杀业务
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@RestController
@RequestMapping("/api/sk")
@Api(tags = "秒杀商品接口", value = "完成商品秒杀核心部分")
@Slf4j
public class SeckillController {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private IGoodsService goodsService;

    @Resource
    private SkMessageSender skMessageSender;

    @Resource
    private IUserService userService;

    @Resource
    private DefaultRedisScript<Long> redisScript;

    @Resource
    private ISkOrderService skOrderService;

    @Resource
    private IAccessRuleService accessRuleService;

//    private final RateLimiter rateLimiter = RateLimiter.create(200);

    /**
     * 库存预热
     * 将所有的商品数量都添加到 redis 中
     * 用户购买商品时，先从 redis 中判断商品库存是否充足
     * 减少去数据库查询的次数，等更新数据库中的库存时，同时更新 redis 中的库存
     * 但在 redis 配置了主从的情况下，校验可能存在问题
     */
    @PostConstruct
    public void goodsStockWarmUp() {
        List<GoodsVo> skGoodsList = goodsService.getGoodsList();
        long goodsCount = goodsService.count();
        GoodsStore.goodsSoldOut = new ConcurrentHashMap<>((int) (goodsCount / 0.75 + 1));
        skGoodsList.forEach(
                skGoods -> {
                    redisTemplate.opsForValue().set(RedisConstant.GOODS_PREFIX + skGoods.getId(), skGoods.getStock());
                    GoodsStore.goodsSoldOut.put(skGoods.getId(), false);
                }
        );
    }

    @ApiOperation("生成秒杀随机地址")
    @GetMapping(value = "/create-skpath")
    public Result<?> getPath(Long userId, Long goodsId) {
        String skPath = skOrderService.createPath(userId, goodsId);
        return Result.success(skPath);
    }

    /**
     * 秒杀商品
     *
     * @return
     */
    @ApiOperation("秒杀操作")
    @PostMapping("/{skPath}/sekillgoods")
    public Result<?> seckillGoods(@PathVariable String skPath, Long userId, Long goodsId) {
        /// 兜底方案之 - 令牌桶限流，两秒内需获取到令牌，否则请求被抛弃
        // 这里用了 synchronize 锁，所以效率会有所降低，暂时不开启
        // if (!rateLimiter.tryAcquire(2, TimeUnit.SECONDS)) {
        //    log.info("被限流了！");
        //    return RestBean.failed(RestBeanEnum.FAILED);
        // }
        User user = userService.getById(userId);
        // 判断秒杀路径是否合法
        boolean isLegalPath = skOrderService.checkPath(userId, goodsId, skPath);
        if(!isLegalPath) {
            return Result.failed(ResultEnum.FAILED, "秒杀路径错误");
        }
        // 判断用户是否有抢购资格
        boolean hasAccessAuthority = accessRuleService.checkAccessAuthority(user, goodsId);
        if(!hasAccessAuthority) {
            return Result.failed(ResultEnum.WITHOUT_ACCESS_AUTHORITY);
        }
        // 判断商品是否卖完
        if (hasSoldOut(goodsId)) {
            log.info("{}号商品已经卖完", goodsId);
            return Result.failed(ResultEnum.GET_GOODS_IS_OVER);
        }
        // 判断用户是否重复秒杀同一商品
        if (hasPurchased(userId, goodsId)) {
            log.info("{}号顾客不能重复秒杀商品", userId);
            return Result.failed(ResultEnum.GET_GOODS_IS_REUSE);
        }
        // 判断商品是否还有库存
        if (!hasStock(goodsId)) {
            log.info("{}号商品已经卖完", goodsId);
            // 内存标记
            GoodsStore.goodsSoldOut.put(goodsId, true);
            return Result.failed(ResultEnum.GET_GOODS_IS_OVER);
        }
        // 创建秒杀信息
        SkMessage skMessage = SkMessage.builder()
                .skUser(user)
                .goodsId(goodsId)
                .build();
        // 将秒杀消息放入消息队列
        skMessageSender.send(JSON.toJSONString(skMessage));
        return Result.success();
    }

    /**
    * 获取秒杀结果
    *        
    * @param userId 秒杀用户 ID
    * @param goodsId 商品id
    * @return 订单 ID
    *
    **/
    @ApiOperation("获取秒杀结果")
    @GetMapping("/getResult")
    public Result<?> getResult(Long userId, Long goodsId) {
        Long orderId = skOrderService.getResult(userId, goodsId);
        return Result.success(orderId);
    }

    /**
     * 判断商品是否卖完
     *
     * @param goodsId 商品 id
     * @return 返回 true 表示该商品已卖完
     */
    private boolean hasSoldOut(long goodsId) {
        Boolean soldOut = GoodsStore.goodsSoldOut.get(goodsId);
        return soldOut != null && soldOut;
    }

    /**
     * 判断用户是否已经购买过某商品
     *
     * @param userId  用户 id
     * @param goodsId 商品 id
     * @return 返回 true 表示当前用户已经购买过该商品
     */
    private boolean hasPurchased(long userId, long goodsId) {
        Boolean hasPurchased = redisTemplate.hasKey(RedisConstant.PURCHASED_USER_PREFIX + userId + goodsId);
        return hasPurchased != null && hasPurchased;
    }

    /**
     * 判断商品是否还有库存
     *
     * @param goodsId 商品 id
     * @return 返回 true 表示当前商品还有库存
     */
    private boolean hasStock(Long goodsId) {
        // redis + lua 扣减库存
        Long stock = redisTemplate.execute(redisScript, Collections.singletonList(RedisConstant.GOODS_PREFIX + goodsId));
        return stock != null && stock >= 0;
    }

}