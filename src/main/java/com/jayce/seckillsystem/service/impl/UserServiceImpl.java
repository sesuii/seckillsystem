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
import com.jayce.seckillsystem.util.WebUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YoungSong
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
    * @Description 创建用户 用户注册
    *
    * @Param [username, identityId, mobilePhone, password]
    * @return
    *
    * @Author YoungSong
    **/
    @Override
    public Result<?> createAccount(String username, String identityId, String mobilePhone, String password) {
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getMobilePhone, mobilePhone)
        );
        if(user != null) return Result.failed(ResultEnum.SAVE_USER_REUSE);
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
        return Result.success(user);
    }


    @Override
    public Result<?> toLogin(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getMobilePhone(), user.getPwd());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UserVo userVo = (UserVo) authentication.getPrincipal();
        redisTemplate.opsForValue().set(RedisConstant.USER_NAME_PREFIX + userVo.getUsername(),
                JSON.toJSONString(userVo), RedisConstant.SESSION_EXPIRE_TIME, TimeUnit.SECONDS);
        String jwt = JwtUtil.createJWT(user.getMobilePhone());
        return Result.success(Map.of("token", jwt));
    }

    /**
     * 分布式存储 session
     *
     * @param mobilePhone 用户名
     * @param user   用户对象
     */
    private void saveSession(String mobilePhone, User user) {
        // 将用户信息保存到 redis 中
        redisTemplate.opsForValue().set(RedisConstant.USER_NAME_PREFIX + mobilePhone, JSON.toJSONString(user), RedisConstant.SESSION_EXPIRE_TIME, TimeUnit.SECONDS);
        // 将用户信息保存到 cookie 中
        HttpServletResponse response = WebUtil.getResponse();
        Cookie cookie = new Cookie(RedisConstant.COOKIE_NAME, mobilePhone);
        cookie.setMaxAge(RedisConstant.SESSION_EXPIRE_TIME);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
