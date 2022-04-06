package com.jayce.seckillsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jayce.seckillsystem.constant.RedisConstant;
import com.jayce.seckillsystem.entity.Goods;
import com.jayce.seckillsystem.entity.OrderInfo;
import com.jayce.seckillsystem.entity.SkGoods;
import com.jayce.seckillsystem.entity.SkOrder;
import com.jayce.seckillsystem.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
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
    private ISkOrderService skOrderService;

    @Resource
    private SkGoodsService skGoodsService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private IGoodsService goodsService;

    @Resource
    private IOrderInfoService orderInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean seckill(Long userId, Long goodsId) throws Exception {
        // 扣减商品库存
        boolean reduceStock = reduceStock(goodsId);
        if (!reduceStock) {
            log.info("{}用户库存扣减失败！", userId);
            throw new Exception("用户库存扣减失败");
        }
        // 生成订单
        boolean add = addOrder(userId, goodsId);
        if (!add) {
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
        Integer redisStock = (Integer) redisTemplate.opsForValue().get(RedisConstant.GOODS_PREFIX + goodsId);
        if (redisStock != null && redisStock <= 0) {
            return false;
        }
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
        Goods goods = goodsService.getById(goodsId);
        boolean isSaveSkOrder, isSaveOrderInfo;
        // 创建订单
        OrderInfo order =OrderInfo.builder()
                .goodsId(goodsId)
                .userId(userId)
                .status((byte) 0)
                .createTime(new Date())
                .goodsPrice(goods.getGoodsPrice())
                .goodsCount(1)
                .goodsName(goods.getGoodsName())
                .build();
        isSaveOrderInfo = orderInfoService.save(order);
        System.out.println(order.getId());
        // 创建秒杀订单
        SkOrder skOrder = SkOrder.builder()
                .orderInfoId(order.getId())
                .userId(userId)
                .goodsId(goodsId)
                .build();

        try {
            isSaveSkOrder = skOrderService.save(skOrder);
        } catch (Exception e) {
            throw e;
        }
        return isSaveSkOrder && isSaveOrderInfo;
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
