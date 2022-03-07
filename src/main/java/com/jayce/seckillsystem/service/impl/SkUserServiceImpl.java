package com.jayce.seckillsystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayce.seckillsystem.constant.RedisConstant;
import com.jayce.seckillsystem.dao.SkUserMapper;
import com.jayce.seckillsystem.domain.SkUser;
import com.jayce.seckillsystem.service.SkUserService;
import com.jayce.seckillsystem.util.WebUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@Service
public class SkUserServiceImpl extends ServiceImpl<SkUserMapper, SkUser> implements SkUserService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean login(String username, String password) {
        SkUser skUser = getOne(
                new LambdaQueryWrapper<SkUser>()
                        .eq(SkUser::getUsername, username)
                        .eq(SkUser::getPassword, password)
        );

        // 用户不存在
        if (skUser == null) {
            return false;
        }

        // 存储用户信息
        saveSession(username, skUser);

        return true;
    }

    /**
     * 分布式存储 session
     *
     * @param username 用户名
     * @param skUser   用户对象
     */
    private void saveSession(String username, SkUser skUser) {
        // 将用户信息保存到 redis 中
        redisTemplate.opsForValue().set(RedisConstant.USER_NAME_PREFIX + username, JSON.toJSONString(skUser), RedisConstant.SESSION_EXPIRE_TIME, TimeUnit.SECONDS);
        // 将用户信息保存到 cookie 中
        HttpServletResponse response = WebUtil.getResponse();
        Cookie cookie = new Cookie(RedisConstant.COOKIE_NAME, username);
        cookie.setMaxAge(RedisConstant.SESSION_EXPIRE_TIME);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
