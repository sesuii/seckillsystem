package com.jayce.seckillsystem.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jayce.seckillsystem.constant.OrderStatusConstant;
import com.jayce.seckillsystem.constant.RabbitmqConstant;
import com.jayce.seckillsystem.dao.OrderInfoMapper;
import com.jayce.seckillsystem.entity.OrderInfo;
import com.jayce.seckillsystem.service.IOrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 死信队列 订单消息接收者
 * @DATE: 2022/4/14 0:02
 * @Author: Gerry
 * @File: seckillsystem OrderInfoReceiever
 * @Email: sjiahui27@gmail.com
 **/
@Component
@RabbitListener(queues = {RabbitmqConstant.DLX_QUEUE})
@Slf4j
public class OrderInfoReceiver {

    @Resource
    OrderInfoMapper orderInfoMapper;

    @Resource
    private IOrderInfoService orderInfoService;

    /**
    * 检查死信队列中订单的状态
    * 如果仍未支付，则修改订单状态，回滚库存
    * @param message 订单信息
    * @return 
    *
    **/
    @RabbitHandler
    public void receive(String message) {
        OrderInfo orderInfo = JSON.parseObject(message, OrderInfo.class);
        Byte orderStatus = orderInfo.getStatus();
        if(orderStatus == OrderStatusConstant.ORDER_JUST_CREATE) {
            log.info("订单未支付已取消");
            orderInfo.setStatus((byte) OrderStatusConstant.ORDER_HAS_CANCELED);
            orderInfoMapper.update(orderInfo, new LambdaQueryWrapper<OrderInfo>()
                    .eq(OrderInfo::getId, orderInfo.getId())
            );
            orderInfoService.rollbackStock(orderInfo.getGoodsId());
            orderInfoService.rollbackRedisStock(orderInfo.getGoodsId());
        }
    }
}
