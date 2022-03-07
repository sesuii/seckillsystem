package com.jayce.seckillsystem.rabbitmq;

import com.jayce.seckillsystem.constant.RabbitmqConstant;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 秒杀消息发送者
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@Component
public class SkMessageSender {
    @Resource
    private AmqpTemplate amqpTemplate;

    /**
     * 发送消息
     *
     * @param message 消息
     */
    public void send(String message) {
        amqpTemplate.convertAndSend(RabbitmqConstant.TOPIC_EXCHANGE_SECKILL, RabbitmqConstant.TOPIC_ROUTING_KEY_SECKILL, message);
    }
}
