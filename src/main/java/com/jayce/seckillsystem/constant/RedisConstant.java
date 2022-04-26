package com.jayce.seckillsystem.constant;

/**
 * 配置与 redis 相关的常量
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
public class RedisConstant {
    /**
     * Token 存活时间
     */
    public static final int TOKEN_EXPIRE_TIME = 1000 * 3600 * 6;

    /**
     * 某商品的秒杀时间（单位：秒）
     */
    public static final int GOODS_EXPIRE_TIME = 60 * 5;

    /**
     * 商品信息存储在 redis 中，以该字符串为前缀
     */
    public static final String GOODS_PREFIX = "goods:";

    /**
     * 用户 ID 为 key 存储在 redis 中，以该字符串为前缀
     */
    public static final String USER_ID_PREFIX = "userId:";

    /**
     * 用户名为 key 存储在 redis 中，以该字符串为前缀
     */
    public static final String USER_NAME_PREFIX = "username:";

    /**
     * 已经秒杀成功的用户 hash 列表
     */
    public static final String PURCHASED_USER_PREFIX = "purchasedUser";


    /**
     * redis 分布式锁名
     */
    public static final String REDIS_LOCK = "lock";
}
