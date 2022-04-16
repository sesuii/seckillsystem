package com.jayce.seckillsystem.filter;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jayce.seckillsystem.constant.RedisConstant;
import com.jayce.seckillsystem.entity.vo.UserVo;
import com.jayce.seckillsystem.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 身份验证过滤器
 * @DATE: 2022/4/15 18:01
 * @Author: Gerry
 * @File: seckillsystem AuthenticationFilter
 * @Email: sjiahui27@gmail.com
 **/
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        Claims claims = JwtUtil.parseJWT(token);
        String mobilePhone = claims.getSubject();
        String s = (String) redisTemplate.opsForValue().get(RedisConstant.USER_NAME_PREFIX + mobilePhone);
        UserVo userVo = JSON.parseObject(s, UserVo.class);
        if(userVo == null) try {
            throw new Exception("用户未登录");
        } catch (Exception e) {
            e.printStackTrace();
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userVo, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
