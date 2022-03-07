package com.jayce.seckillsystem.constant;

/**
 * 配置与 Rabbitmq 相关的常量
 *
 * @author <a href="mailto:su_1999@126.com">sujian</a>
 */
public class RabbitmqConstant {
    /**
     * 秒杀交换机
     */
    public static final String TOPIC_EXCHANGE_SECKILL = "seckill_exchange";

    /**
     * 秒杀队列
     */
    public static final String SECKILL_QUEUE = "seckill_queue";

    /**
     *
     */
    public static final String TOPIC_ROUTING_KEY_SECKILL = "topic.seckill";

}
