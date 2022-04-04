package com.jayce.seckillsystem.service;

import com.jayce.seckillsystem.entity.OrderInfo;

/**
 * SeckillService
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
public interface SeckillService {
    /**
     * 秒杀商品
     *
     * @param userId  用户ID
     * @param goodsId 商品ID
     */
    boolean seckill(Long userId, Long goodsId) throws Exception;
}
