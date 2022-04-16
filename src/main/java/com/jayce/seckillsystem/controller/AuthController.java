package com.jayce.seckillsystem.controller;


import com.jayce.seckillsystem.constant.ResultEnum;
import com.jayce.seckillsystem.entity.User;
import com.jayce.seckillsystem.entity.resp.Result;
import com.jayce.seckillsystem.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "用户认证接口", value = "包括用户登录、注册操作")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    IUserService userService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<?> register(String username,
                              String identityId,
                              String mobilePhone,
                              String password) {
        return  userService.createAccount(username, identityId, mobilePhone, password);
    }

    @ApiOperation("用户登录")
    @PostMapping(value = "/toLogin")
    public Result<?> toLogin(@RequestBody User user) {
        return userService.toLogin(user);
    }

    @ApiOperation("退出登录")
    @GetMapping("/logout-success")
    public Result<?> logoutSuccess() {
        return Result.success();
    }

    @ApiOperation("未登录拒绝访问")
    @RequestMapping("/access-deny")
    public Result<?> accessDeny() {
        return Result.failed(ResultEnum.AUTH_DENY);
    }
}
