package com.jayce.seckillsystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayce.seckillsystem.constant.RedisConstant;
import com.jayce.seckillsystem.constant.ResultEnum;
import com.jayce.seckillsystem.dao.UserMapper;
import com.jayce.seckillsystem.entity.User;
import com.jayce.seckillsystem.entity.resp.Result;
import com.jayce.seckillsystem.entity.vo.UserVo;
import com.jayce.seckillsystem.service.IUserService;
import com.jayce.seckillsystem.util.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Gerry
 * @since 2022-03-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    UserMapper userMapper;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    AuthenticationManager authenticationManager;

    /**
     * @return
     * @Description 创建用户 用户注册
     * @Param [username, identityId, mobilePhone, password]
     * @Author Gerry
     **/
    @Override
    public User createAccount(String username, String identityId, String mobilePhone, String password) {
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getMobilePhone, mobilePhone)
        );
        if (user != null) return null;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user = User.builder()
                .realname(username)
                .identityId(identityId)
                .mobilePhone(mobilePhone)
                .pwd(encoder.encode(password))
                .lastLoginIp("0.0.0.0")
                .build();
        userMapper.insert(user);
        user = userMapper.selectById(user.getId());
        return user;
    }

    /**
    * @Description 用户登录，成功返回 Token
    *
    * @param user 用户
    * @return
    *
    **/
    @Override
    public Map<String, String> toLogin(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getMobilePhone(), user.getPwd());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UserVo userVo = (UserVo) authentication.getPrincipal();
        redisTemplate.opsForValue().set(RedisConstant.USER_NAME_PREFIX + userVo.getUsername(),
                JSON.toJSONString(userVo), RedisConstant.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        String jwt = JwtUtil.createJWT(user.getMobilePhone());
        return Map.of("token", jwt);
    }
}

