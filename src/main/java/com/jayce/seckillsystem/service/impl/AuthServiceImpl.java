package com.jayce.seckillsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jayce.seckillsystem.entity.User;
import com.jayce.seckillsystem.entity.vo.UserVo;
import com.jayce.seckillsystem.service.IUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AuthServiceImpl implements UserDetailsService {

    @Resource
    IUserService UserService;

    @Override
    public UserDetails loadUserByUsername(String mobilePhone) throws UsernameNotFoundException {
        User user = UserService.getOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getMobilePhone, mobilePhone)
        );
        if(user == null) throw new UsernameNotFoundException("该用户不存在");
        return UserVo.builder()
                .user(user)
                .build();
    }

}
