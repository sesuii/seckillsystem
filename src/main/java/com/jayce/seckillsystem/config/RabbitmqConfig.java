package com.jayce.seckillsystem.config;

import com.jayce.seckillsystem.constant.RabbitmqConstant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.jayce.seckillsystem.constant.RabbitmqConstant.TOPIC_ROUTING_KEY_PAYMENT;


/**
 * Rabbitmq 配置
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@Configuration
public class RabbitmqConfig {

    // 秒杀 异步下单队列及交换机绑定
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

    // 支付 延迟队列及交换机绑定
    @Bean
    public TopicExchange ttlExchange() {
        return new TopicExchange(RabbitmqConstant.TTL_EXCHANGE);
    }

    @Bean
    public Queue ttlQueue() {
        return QueueBuilder.durable(RabbitmqConstant.TTL_QUEUE)
                .ttl(RabbitmqConstant.ORDER_EXIST_TIME)
                .deadLetterExchange(RabbitmqConstant.DLX_EXCHANGE)
                .deadLetterRoutingKey(TOPIC_ROUTING_KEY_PAYMENT)
                // 队列最大消息数量为10
                .maxLength(10)
                .build();
    }
    /**
     * 声明死信交换机
     *
     * @return 死信交换机
     */
    @Bean
    public TopicExchange dlxExchange() {
        return new TopicExchange(RabbitmqConstant.DLX_EXCHANGE);
    }

    /**
     * 声明死信队列
     *
     * @return 死信队列
     */
    @Bean
    public Queue dlxQueue() {
        return new Queue(RabbitmqConstant.DLX_QUEUE);
    }

    /**
     * 绑定ttl队列到ttl交换机
     *
     * @param queue    ttl队列
     * @param exchange ttl交换机
     */
    @Bean
    public Binding bindingTtl(@Qualifier("ttlQueue") Queue queue,
                              @Qualifier("ttlExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("ttl.#").noargs();
    }

    /**
     * 绑定死信队列到死信交换机
     *
     * @param queue    死信队列
     * @param exchange 死信交换机
     */
    @Bean
    public Binding bindingDlx(@Qualifier("dlxQueue") Queue queue,
                              @Qualifier("dlxExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("dlx.#").noargs();
    }
}
