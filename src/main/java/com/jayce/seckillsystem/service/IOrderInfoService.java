package com.jayce.seckillsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayce.seckillsystem.entity.OrderInfo;
import com.jayce.seckillsystem.entity.TradeRecord;
import com.jayce.seckillsystem.entity.resp.Result;
import com.jayce.seckillsystem.entity.vo.OrderInfoVo;

/**
 * <p>
 *  订单管理服务类
 * </p>
 *
 * @author YoungSong
 * @since 2022-03-23
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    /**
     * 获取订单详情信息
     *
     * @param orderId 订单号
     * @return
     **/
    OrderInfoVo toDetail(Long orderId);

    /**
     * 支付订单
     *
     * @param order 订单
     * @return
     **/
    boolean payOrder(OrderInfo order) throws Exception;

    /**
     * 取消订单 删除秒杀订单内容 将订单状态设为 取消状态
     *
     * @param orderInfo 订单
     * @return
     *
     **/
    boolean cancelOrder(OrderInfo orderInfo);

    /**
     * 回滚 Redis 预减库存
     *
     * @param goodsId 商品 ID
     * @return
     *
     **/
    boolean rollbackRedisStock(Long goodsId);

    /**
     * 回滚数据库库存
     *
     * @param goodsId 商品 ID
     * @return
     *
     **/
    boolean rollbackStock(Long goodsId);

    /**
     * 查看交易记录
     *
     * @param orderId 订单ID
     * @return
     *
     **/
    TradeRecord getTradeRecord(Long orderId);

}
