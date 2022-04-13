package com.jayce.seckillsystem.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jayce.seckillsystem.constant.RabbitmqConstant;
import com.jayce.seckillsystem.dao.OrderInfoMapper;
import com.jayce.seckillsystem.entity.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description: 死信队列 订单消息接收者
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

    @RabbitHandler
    public void receive(String message) {
        OrderInfo orderInfo = JSON.parseObject(message, OrderInfo.class);
        Byte orderStatus = orderInfo.getStatus();
        if(orderStatus == (byte) 0) {
            log.info("订单未支付已取消");
            orderInfo.setStatus((byte) 2);
            orderInfoMapper.update(orderInfo, new LambdaQueryWrapper<OrderInfo>()
                    .eq(OrderInfo::getId, orderInfo.getId())
            );
        }
    }
}
