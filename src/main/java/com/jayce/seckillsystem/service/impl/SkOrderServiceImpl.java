package com.jayce.seckillsystem.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayce.seckillsystem.dao.SkOrderMapper;
import com.jayce.seckillsystem.entity.SkOrder;
import com.jayce.seckillsystem.entity.User;
import com.jayce.seckillsystem.service.ISkOrderService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@Service
public class SkOrderServiceImpl extends ServiceImpl<SkOrderMapper, SkOrder> implements ISkOrderService {


    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String createPath(User user, Long goodsId) {
        String str = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" + goodsId, str, 1, TimeUnit.MINUTES);
        return str;
    }

    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if (user == null || goodsId < 0 || StringUtils.isEmpty(path)) {
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }
}
