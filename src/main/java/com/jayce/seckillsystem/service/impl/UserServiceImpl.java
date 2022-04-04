package com.jayce.seckillsystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayce.seckillsystem.constant.RedisConstant;
import com.jayce.seckillsystem.constant.RestBeanEnum;
import com.jayce.seckillsystem.dao.UserMapper;
import com.jayce.seckillsystem.entity.User;
import com.jayce.seckillsystem.entity.resp.RestBean;
import com.jayce.seckillsystem.entity.vo.UserVo;
import com.jayce.seckillsystem.service.IUserService;
import com.jayce.seckillsystem.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
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

    /**
    * @Description 创建用户 用户注册
    *
    * @Param [username, identityId, mobilePhone, password]
    * @return
    *
    * @Author YoungSong
    **/
    @Override
    public RestBean<?> createAccount(String username, String identityId, String mobilePhone, String password) {
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getIdentityId, identityId)
        );
        if(user != null) return RestBean.failed(RestBeanEnum.SAVE_USER_REUSE);
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
        return RestBean.success(user);
    }

    @Override
    public RestBean<?> doLongin(UserVo userVo, HttpServletRequest request, HttpServletResponse response) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String mobilePhone = userVo.getMobile();
        String password = userVo.getPassword();
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getMobilePhone, mobilePhone)
        );
        if(user == null) {
            return RestBean.failed(RestBeanEnum.GET_USER_NOT_FOUND);
        }
        if(!encoder.matches(password, user.getPwd())) {
            return RestBean.failed(RestBeanEnum.USER_PASSWORD_ERROR);
        }
        saveSession(mobilePhone, user);
        user.setPwd(null);
        return RestBean.success(user);
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
