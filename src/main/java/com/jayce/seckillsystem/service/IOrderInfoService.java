package com.jayce.seckillsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayce.seckillsystem.entity.OrderInfo;
import com.jayce.seckillsystem.entity.resp.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YoungSong
 * @since 2022-03-23
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    /**
    * @Description 获取订单详细信息
    *
    * @param orderId 订单号
    * @return
    *
    **/
    Result<?> detail(Long orderId);

    /**
     * 支付订单 更新个人账户 更新银行账户 生成交易记录
     * @param orderId 订单号
     * @return
     * @throws Exception
     */
    Result<?> payOrder(Long orderId) throws Exception;

    /**
    * @Description 取消订单
    *
    * @param orderInfo 订单
    * @return
    *
    **/
    Result<?> cancelOrder(OrderInfo orderInfo) throws Exception;
}
