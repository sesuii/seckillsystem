package com.jayce.seckillsystem.config;

import com.jayce.seckillsystem.constant.RabbitmqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Rabbitmq 配置
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@Configuration
public class RabbitmqConfig {

    @Bean
    public Queue seckillQueue() {
        return new Queue(RabbitmqConstant.SECKILL_QUEUE);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(RabbitmqConstant.TOPIC_EXCHANGE_SECKILL);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(seckillQueue()).to(topicExchange()).with(RabbitmqConstant.TOPIC_ROUTING_KEY_SECKILL);
    }

}
