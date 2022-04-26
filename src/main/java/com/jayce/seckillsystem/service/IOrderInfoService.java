package com.jayce.seckillsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayce.seckillsystem.entity.OrderInfo;
import com.jayce.seckillsystem.entity.resp.Result;
import com.jayce.seckillsystem.entity.vo.OrderInfoVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YoungSong
 * @since 2022-03-23
 */
public interface IOrderInfoService extends IService<OrderInfo> {


    OrderInfoVo toDetail(Long orderId);


    Result<?> payOrder(Long orderId) throws Exception;

    boolean cancelOrder(OrderInfo orderInfo) throws Exception;

    void rollbackRedisStock(Long goodsId);

    boolean rollbackStock(Long goodsId);
}
