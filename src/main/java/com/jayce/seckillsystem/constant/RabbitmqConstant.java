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
     * 秒杀队列 routing key
     */
    public static final String TOPIC_ROUTING_KEY_SECKILL = "topic.seckill";

    /**
     * ttl交换机
     */
    public static final String TTL_EXCHANGE = "order_ttl_exchange";

    /**
     * ttl队列
     */
    public static final String TTL_QUEUE = "order_ttl_queue";

    /**
     * 死信交换机
     */
    public static final String DLX_EXCHANGE = "order_dlx_exchange";

    /**
     * 死信队列
     */
    public static final String DLX_QUEUE = "order_dlx_queue";

    /**
     * 延迟队列 routing key
     */
    public static final String TOPIC_ROUTING_KEY_PAYMENT = "dlx.payment";

    /**
     * 未支付订单存活时间
     */
    public static final int ORDER_EXIST_TIME = 15 * 60 * 1000;

}
