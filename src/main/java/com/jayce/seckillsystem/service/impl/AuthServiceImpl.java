package com.jayce.seckillsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jayce.seckillsystem.service.IUserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AuthServiceImpl implements UserDetailsService {

    @Resource
    IUserService iUserService;

    @Override
    public UserDetails loadUserByUsername(String mobilePhone) throws UsernameNotFoundException {
        com.jayce.seckillsystem.entity.User user = iUserService.getOne(
                new LambdaQueryWrapper<com.jayce.seckillsystem.entity.User>()
                        .eq(com.jayce.seckillsystem.entity.User::getMobilePhone, mobilePhone)
        );
        if(user == null) throw new UsernameNotFoundException("");
        return User
                .withUsername(user.getMobilePhone())
                .password(user.getPwd())
                .roles("user")
                .build();
    }

}
