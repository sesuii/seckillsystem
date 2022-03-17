package com.jayce.seckillsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jayce.seckillsystem.dao.AccountMapper;
import com.jayce.seckillsystem.entity.Account;
import com.jayce.seckillsystem.entity.resp.RestBean;
import com.jayce.seckillsystem.service.AccountService;
import com.jayce.seckillsystem.service.SkUserService;
import com.jayce.seckillsystem.service.impl.AccountServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 登录管理
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
public class AccountController {
    @Resource
    private SkUserService skUserService;
    @Resource
    private AccountService accountService;

    @GetMapping("/info")
    public RestBean<Account> info() {
        SecurityContext context = SecurityContextHolder.getContext();
        Account account = accountService.getOne(
                new LambdaQueryWrapper<Account>()
                        .eq(Account::getMobilePhone, context.getAuthentication().getName())
        );
        return new RestBean<>(200, "获取用户信息成功", account );
    }

    @PostMapping("/toDetail")
    public String toDetail(@RequestParam("username") String username, @RequestParam("password") String password) {
        boolean login = skUserService.login(username, password);
        if (login) {
            log.info("登录成功！");
            return "detail";
        }
        return "login";
    }
}
