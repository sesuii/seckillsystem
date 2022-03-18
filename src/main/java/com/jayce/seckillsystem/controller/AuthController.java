package com.jayce.seckillsystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jayce.seckillsystem.entity.Account;
import com.jayce.seckillsystem.entity.resp.RestBean;
import com.jayce.seckillsystem.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "用户认证接口", description = "包括用户登录、注册操作")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Resource
    private AccountService accountService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public RestBean<Void> register(String username,
                                   String identityId,
                                   String mobilePhone,
                                   String password) {
        accountService.createAccount(username, identityId, mobilePhone, password);
        return new RestBean<>(200, "注册账号成功！");
    }
    @ApiOperation("登录成功")
    @PostMapping("/login-success")
    public RestBean<Void> loginSuccess() {
        return new RestBean<>(200, "登录成功！");
    }
    @ApiOperation("登录失败")
    @PostMapping("/login-failure")
    public RestBean<Void> login() {
        return new RestBean<>(304, "登录失败！");
    }
    @ApiOperation("退出登录")
    @GetMapping("/logout-success")
    public RestBean<Void> logoutSuccess() {
        return new RestBean<>(200, "退出成功");
    }
    @ApiOperation("未登录拒绝访问")
    @RequestMapping("/access-deny")
    public RestBean<Void> accessDeny() {
        return new RestBean<>(401, "请先进行登录再操作");
    }

}
