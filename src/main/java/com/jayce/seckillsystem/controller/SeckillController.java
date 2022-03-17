package com.jayce.seckillsystem.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.google.common.util.concurrent.RateLimiter;
import com.jayce.seckillsystem.constant.RedisConstant;
import com.jayce.seckillsystem.entity.GoodsStore;
import com.jayce.seckillsystem.entity.SkGoods;
import com.jayce.seckillsystem.entity.SkMessage;
import com.jayce.seckillsystem.entity.SkUser;
import com.jayce.seckillsystem.rabbitmq.SkMessageSender;
import com.jayce.seckillsystem.service.SkGoodsService;
import com.jayce.seckillsystem.util.RedisLock;
import com.jayce.seckillsystem.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
@Controller
@RequestMapping("/seckill")
@Slf4j
public class SeckillController {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private SkGoodsService skGoodsService;

    @Resource
    private SkMessageSender skMessageSender;

    @Resource
    private DefaultRedisScript<Long> redisScript;

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
        List<SkGoods> skGoodsList = skGoodsService.list();
        long goodsCount = skGoodsService.count();
        GoodsStore.goodsSoldOut = new ConcurrentHashMap<>((int) (goodsCount / 0.75 + 1));
        skGoodsList.forEach(
                skGoods -> {
                    redisTemplate.opsForValue().set(RedisConstant.GOODS_PREFIX + skGoods.getId(), skGoods.getStock());
                    GoodsStore.goodsSoldOut.put(skGoods.getId(), false);
                }
        );
    }

    /**
     * 秒杀商品
     *
     * @return
     */
    @PostMapping("/goods")
    public String seckillGoods(@RequestParam("goodsId") Long goodsId) {
        // 判断用户是否登录
//        SkUser skUser = isLogin();
//        if (skUser == null) {
//            log.info("用户未登录！");
//            return "login";
//        }

        // 兜底方案之 - 令牌桶限流，两秒内需获取到令牌，否则请求被抛弃
        // 这里用了 synchronize 锁，所以效率会有所降低
//        if (!rateLimiter.tryAcquire(2, TimeUnit.SECONDS)) {
//            log.info("被限流了！");
//            return "fail";
//        }

        // 使用随机字数字模拟 userId
        long userId = new Random().nextInt(2000);

        // 判断商品是否卖完了
        if (hasSoldOut(goodsId)) {
            log.info("{}号商品已经卖完", goodsId);
            return "fail";
        }

        // 判断用户是否重复秒杀某一商品
        if (hasPurchased(userId, goodsId)) {
            log.info("{}号顾客不能重复秒杀商品", userId);
            return "fail";
        }

        // 判断商品是否还有库存
        if (!hasStock(goodsId)) {
            // 标记商品已经卖完了
            log.info("{}号商品已经卖完", goodsId);
            GoodsStore.goodsSoldOut.put(goodsId, true);
            return "fail";
        }

        // 创建秒杀信息
        SkMessage skMessage = SkMessage.builder()
                .userId(userId)
                .goodsId(goodsId)
                .build();

        // 将秒杀消息放入消息队列
        skMessageSender.send(JSON.toJSONString(skMessage));

        return "success";
    }

    /**
     * 判断用户是否登录
     *
     * @return 返回 null 表示未登录
     */
    private SkUser isLogin() {
        HttpServletRequest request = WebUtil.getRequest();
        Cookie[] cookies = request.getCookies();
        if (ArrayUtils.isEmpty(cookies)) {
            return null;
        }
        Optional<Object> skUserStr = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(RedisConstant.COOKIE_NAME))
                .map(cookie -> redisTemplate.opsForValue().get(cookie.getValue()))
                .findFirst();
        return skUserStr.map(s -> JSON.parseObject(s.toString(), SkUser.class)).orElse(null);
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