package com.jayce.seckillsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jayce.seckillsystem.entity.Account;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AuthService implements UserDetailsService {

    @Resource
    AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String mobilePhone) throws UsernameNotFoundException {
        Account account = accountService.getOne(
                new LambdaQueryWrapper<Account>()
                        .eq(Account::getMobilePhone, mobilePhone)
        );
        if(account == null) throw new UsernameNotFoundException("");
        return User
                .withUsername(account.getMobilePhone())
                .password(account.getPassword())
                .roles("user")
                .build();
    }
}
