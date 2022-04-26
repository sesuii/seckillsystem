package com.jayce.seckillsystem.config;

import com.jayce.seckillsystem.constant.RabbitmqConstant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.jayce.seckillsystem.constant.RabbitmqConstant.TOPIC_ROUTING_KEY_PAYMENT;


/**
 * Rabbitmq 配置
 * 秒杀消息队列以及延迟队列配置
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

    @Bean
    public TopicExchange ttlExchange() {
        return new TopicExchange(RabbitmqConstant.TTL_EXCHANGE);
    }

    /**
    * @Description TTL 队列配置
    * 订单存活时间为 15 分钟
    * @return
    *
    **/
    @Bean
    public Queue ttlQueue() {
        return QueueBuilder.durable(RabbitmqConstant.TTL_QUEUE)
                .ttl(RabbitmqConstant.ORDER_EXIST_TIME)
                .deadLetterExchange(RabbitmqConstant.DLX_EXCHANGE)
                .deadLetterRoutingKey(TOPIC_ROUTING_KEY_PAYMENT)
                .build();
    }

    @Bean
    public TopicExchange dlxExchange() {
        return new TopicExchange(RabbitmqConstant.DLX_EXCHANGE);
    }

    @Bean
    public Queue dlxQueue() {
        return new Queue(RabbitmqConstant.DLX_QUEUE);
    }

    /**
    * @Description 绑定 TTL 队列到 TTL 交换机
    *
    * @param queue TTL 队列
    * @param exchange TTL 交换机
    * @return
    *
    **/
    @Bean
    public Binding bindingTtl(@Qualifier("ttlQueue") Queue queue,
                              @Qualifier("ttlExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("ttl.#").noargs();
    }

    /**
    * @Description 绑定死信队列到死信交换机
    *
    * @param queue DLX 死信队列
    * @param exchange DLX 死信交换机
    * @return
    *
    **/
    @Bean
    public Binding bindingDlx(@Qualifier("dlxQueue") Queue queue,
                              @Qualifier("dlxExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("dlx.#").noargs();
    }
}
