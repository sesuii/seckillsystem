package com.jayce.seckillsystem.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.jayce.seckillsystem.constant.RabbitmqConstant;
import com.jayce.seckillsystem.constant.RedisConstant;
import com.jayce.seckillsystem.entity.GoodsStore;
import com.jayce.seckillsystem.entity.SkMessage;
import com.jayce.seckillsystem.service.IOrderInfoService;
import com.jayce.seckillsystem.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消息接收者
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@Component
@RabbitListener(queues = {RabbitmqConstant.SECKILL_QUEUE})
@Slf4j
public class SkMessageReceiver {

    @Resource
    private SeckillService seckillService;

    @Resource
    IOrderInfoService orderInfoService;

    @RabbitHandler
    public void receive(String message) {
        // 转换成秒杀消息
        SkMessage skMessage = JSON.parseObject(message, SkMessage.class);
        // 秒杀商品（由于数据库设置了唯一索引，生成订单的时候，可能发生异常）
        try {
            seckillService.seckill(skMessage.getSkUser().getId(), skMessage.getGoodsId());
        } catch (Throwable e) {
            // 用户最终秒杀商品失败，将预减的库存加回来
            orderInfoService.rollbackRedisStock(skMessage.getGoodsId());
            log.error("=====****===== " + e.getMessage());
        }
    }
}
