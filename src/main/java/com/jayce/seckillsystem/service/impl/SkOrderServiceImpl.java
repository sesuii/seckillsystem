package com.jayce.seckillsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayce.seckillsystem.dao.SkOrderMapper;
import com.jayce.seckillsystem.entity.GoodsStore;
import com.jayce.seckillsystem.entity.SkOrder;
import com.jayce.seckillsystem.entity.User;
import com.jayce.seckillsystem.service.ISkOrderService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@Service
public class SkOrderServiceImpl extends ServiceImpl<SkOrderMapper, SkOrder> implements ISkOrderService {

    @Resource
    private ISkOrderService skOrderService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String createPath(Long userId, Long goodsId) {
        String str = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set("seckillPath:" + userId + ":" + goodsId, str, 3, TimeUnit.MINUTES);
        return str;
    }

    @Override
    public boolean checkPath(Long userId, Long goodsId, String path) {
        if (goodsId < 0 || StringUtils.isEmpty(path)) {
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + userId + ":" + goodsId);
        System.out.println(redisPath);
        return path.equals(redisPath);
    }

    @Override
    public Long getResult(Long userId, Long goodsId) {
        SkOrder skOrder = skOrderService.getOne(
                new LambdaQueryWrapper<SkOrder>()
                        .eq(SkOrder::getUserId, userId)
                        .eq(SkOrder::getGoodsId, goodsId)
        );
        if (null != skOrder) {
            return skOrder.getOrderInfoId();
        } else if (GoodsStore.goodsSoldOut.get(goodsId)) {
            return -1L;
        } else {
            return 0L;
        }
    }
}
