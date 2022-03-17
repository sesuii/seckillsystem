package com.jayce.seckillsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jayce.seckillsystem.constant.RedisConstant;
import com.jayce.seckillsystem.entity.SkGoods;
import com.jayce.seckillsystem.entity.SkOrder;
import com.jayce.seckillsystem.service.SeckillService;
import com.jayce.seckillsystem.service.SkGoodsService;
import com.jayce.seckillsystem.service.SkOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * SeckillServiceImpl
 *
 * @author <a href="mailto:su_1999@126.com">sujian</a>
 */
@Service
@Slf4j
public class SeckillServiceImpl implements SeckillService {
    @Resource
    private SkOrderService skOrderService;

    @Resource
    private SkGoodsService skGoodsService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean seckill(Long userId, Long goodsId) throws Exception {

        // 判断库存是否充足 (已优化为在 redis 中预减库存)
//        boolean checkStock = checkStock(goodsId);
//        if (!checkStock) {
//            log.info("{}用户库存不足！", userId);
//            return false;
//        }

        // 判断用户是否重复秒杀（已优化为在 redis 中存储用户与商品信息）
//        boolean repeatSeckill = repeatSeckill(userId, goodsId);
//        if (repeatSeckill) {
//            log.info("{}用户不能重复秒杀同一商品！", userId);
//            return false;
//        }

        // 扣减商品库存
        boolean reduceStock = reduceStock(goodsId);
        if (!reduceStock) {
            log.info("{}用户库存扣减失败！", userId);
            throw new Exception("用户库存扣减失败");
        }

        // 生成订单
        boolean addOrder = addOrder(userId, goodsId);

        if (!addOrder) {
            log.info("{}用户订单生成失败！", userId);
            throw new Exception("用户订单生成失败");
        }
        // 以互斥锁的形式将 用户ID 与 商品ID 保存到 redis 中，用于判断用户是否重复秒杀
        redisTemplate.opsForValue().setIfAbsent(RedisConstant.PURCHASED_USER_PREFIX + userId + goodsId, 1, RedisConstant.GOODS_EXPIRE_TIME, TimeUnit.SECONDS);
        log.info("{}号用户秒杀成功", userId);
        return true;
    }

    /**
     * 校验库存
     * 先从 redis 中获取商品库存，减少去数据库查询库存的次数
     * 如 redis 中查询到的库存足够，再从数据库中获取库存
     *
     * @param goodsId 商品ID
     * @return true 表示库存足够
     */
    private boolean checkStock(Long goodsId) {
        // 先从 redis 中获取商品库存，减少去数据库查询库存的次数
//        Integer redisStock = (Integer) redisTemplate.opsForValue().get(RedisConstant.GOODS_PREFIX + goodsId);
//        if (redisStock != null && redisStock <= 0) {
//            return false;
//        }
        // 从数据库中获取库存
        int stock = skGoodsService.getStock(goodsId);
        return stock > 0;
    }

    /**
     * 扣减商品库存
     *
     * @param goodsId 商品ID
     * @return 1 表示扣减库存成功，否则表示失败
     */
    private boolean reduceStock(Long goodsId) {
        SkGoods skGoods = skGoodsService.getById(goodsId);
        int newStock = skGoods.getStock() - 1;
        skGoods.setStock(newStock);
        // 更新数据库的库存
        int update = skGoodsService.reduceStock(skGoods);
        return update == 1;
    }

    /**
     * 添加订单
     *
     * @param userId  用户ID
     * @param goodsId 商品ID
     * @return
     */
    private boolean addOrder(Long userId, Long goodsId) {
        SkGoods skGoods = skGoodsService.getById(goodsId);
        SkOrder skOrder = SkOrder.builder()
                .userId(userId)
                .goodsId(goodsId)
                .build();
        boolean save;
        try {
            save = skOrderService.save(skOrder);
        } catch (Exception e) {
            throw e;
        }
        return save;
    }

    /**
     * 判断同一用户是否重复秒杀同一件商品
     *
     * @param userId  用户ID
     * @param goodsId 商品ID
     * @return 返回 true，表示用户重复秒杀同一件商品
     */
    private boolean repeatSeckill(Integer userId, Long goodsId) {
        Boolean hasUser = redisTemplate.hasKey(userId + "__" + goodsId);
        if (hasUser != null && !hasUser) {
            return false;
        }
        SkOrder skOrder = skOrderService.getOne(
                new LambdaQueryWrapper<SkOrder>()
                        .eq(SkOrder::getUserId, userId)
                        .eq(SkOrder::getGoodsId, goodsId)
        );
        return skOrder != null;
    }
}
