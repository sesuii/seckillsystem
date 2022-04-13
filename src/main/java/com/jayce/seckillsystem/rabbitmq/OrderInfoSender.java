package com.jayce.seckillsystem.rabbitmq;

import com.jayce.seckillsystem.constant.RabbitmqConstant;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description: 订单信息发送者
 * @DATE: 2022/4/13 23:56
 * @Author: Gerry
 * @File: seckillsystem OrderInfoSender
 * @Email: sjiahui27@gmail.com
 **/

@Component
public class OrderInfoSender {
    @Resource
    private AmqpTemplate amqpTemplate;
    /**
    * @Description: 订单信息发送
    *
    * @param message 订单
    * @return
    *
    **/
    public void send(String message) {
        System.out.println(message);
        amqpTemplate.convertAndSend(RabbitmqConstant.TTL_EXCHANGE, "ttl.order", message);
    }
}
