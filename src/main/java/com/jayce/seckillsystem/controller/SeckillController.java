package com.jayce.seckillsystem.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.google.common.util.concurrent.RateLimiter;
import com.jayce.seckillsystem.constant.RedisConstant;
import com.jayce.seckillsystem.constant.RestBeanEnum;
import com.jayce.seckillsystem.entity.GoodsStore;
import com.jayce.seckillsystem.entity.SkMessage;
import com.jayce.seckillsystem.entity.User;
import com.jayce.seckillsystem.entity.resp.RestBean;
import com.jayce.seckillsystem.entity.vo.GoodsVo;
import com.jayce.seckillsystem.rabbitmq.SkMessageSender;
import com.jayce.seckillsystem.service.IGoodsService;
import com.jayce.seckillsystem.service.IUserService;
import com.jayce.seckillsystem.util.RedisLock;
import com.jayce.seckillsystem.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 秒杀业务
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@RestController
@RequestMapping("/api")
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
    private DefaultRedisScript<Long> redisScript;

    @Resource
    private IUserService userService;

    @Resource
    private RedisLock redisLock;

    private RateLimiter rateLimiter = RateLimiter.create(200);

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
    @GetMapping(value = "/skPath")
    public RestBean<?> getPath(User user, Long goodsId, String captcha, HttpServletRequest request) {
        if (user == null) {
            return RestBean.failed(RestBeanEnum.AUTH_DENY);
        }
//        boolean check = orderService.checkCaptcha(user, goodsId, captcha);
//        if (!check) {
//            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
//        }
//        String path = orderService.createPath(user, goodsId);
//        return RespBean.success(path);
        return null;
    }


    /**
     * 秒杀商品
     *
     * @return
     */
    @ApiOperation("秒杀操作")
    @PostMapping("/sekillgoods")
    public RestBean<?> seckillGoods(@RequestParam("goodsId") Long goodsId) {
//        User user = isLogin();
//        if (user == null) {
//            return RestBean.failed(RestBeanEnum.AUTH_DENY);
//        }

        // 验证路径是否有效？

//        // 兜底方案之 - 令牌桶限流，两秒内需获取到令牌，否则请求被抛弃
//        // 这里用了 synchronize 锁，所以效率会有所降低
//        if (!rateLimiter.tryAcquire(2, TimeUnit.SECONDS)) {
//            log.info("被限流了！");
//            return RestBean.failed(RestBeanEnum.FAILED);
//        }
        User user = User.builder()
                .id((long) new Random().nextInt(100))
                .build();

        // 判断商品是否卖完了
        if (hasSoldOut(goodsId)) {
            log.info("{}号商品已经卖完", goodsId);
            return RestBean.failed(RestBeanEnum.GET_GOODS_IS_OVER);
        }

        // 判断用户是否重复秒杀某一商品
        if (hasPurchased(user.getId(), goodsId)) {
            log.info("{}号顾客不能重复秒杀商品", user.getId());
            return RestBean.failed(RestBeanEnum.GET_GOODS_IS_REUSE);
        }

        // 判断商品是否还有库存
        if (!hasStock(goodsId)) {
            // 标记商品已经卖完了
            log.info("{}号商品已经卖完", goodsId);
            GoodsStore.goodsSoldOut.put(goodsId, true);
            return RestBean.failed(RestBeanEnum.GET_GOODS_IS_OVER);
        }

        // 创建秒杀信息
        SkMessage skMessage = SkMessage.builder()
                .skUser(user)
                .goodsId(goodsId)
                .build();

//         将秒杀消息放入消息队列
        skMessageSender.send(JSON.toJSONString(skMessage));

        return RestBean.success(user);
    }

    /**
     * 判断用户是否登录
     *
     * @return 返回 null 表示未登录
     */
    private User isLogin() {
        HttpServletRequest request = WebUtil.getRequest();
        Cookie[] cookies = request.getCookies();
        if (ArrayUtils.isEmpty(cookies)) {
            return null;
        }
        Optional<Object> skUserStr = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(RedisConstant.COOKIE_NAME))
                .map(cookie -> redisTemplate.opsForValue().get(cookie.getValue()))
                .findFirst();
        return skUserStr.map(s -> JSON.parseObject(s.toString(), User.class)).orElse(null);
    }

    /**
     * 判断商品是否卖完了
     *
     * @param goodsId 商品 id
     * @return 返回 true 表示该商品卖完了
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