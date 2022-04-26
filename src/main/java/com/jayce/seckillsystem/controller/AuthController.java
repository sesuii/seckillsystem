package com.jayce.seckillsystem.controller;


import com.jayce.seckillsystem.constant.ResultEnum;
import com.jayce.seckillsystem.entity.User;
import com.jayce.seckillsystem.entity.resp.Result;
import com.jayce.seckillsystem.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

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
        User user = userService.createAccount(username, identityId, mobilePhone, password);
        if(user == null) return Result.failed(ResultEnum.SAVE_USER_REUSE);
        return  Result.success(user);
    }

    @ApiOperation("用户登录")
    @PostMapping(value = "/to-login")
    public Result<?> toLogin(@RequestBody User user) {
        Map<String, String> token = userService.toLogin(user);
        return Result.success(token);
    }

    @ApiOperation("退出登录")
    @GetMapping("/logout-success")
    public Result<?> logoutSuccess() {
        return Result.success();
    }

    @ApiOperation("未登录拒绝访问")
    @PostMapping("/access-deny")
    public Result<?> accessDeny() {
        return Result.failed(ResultEnum.AUTH_DENY);
    }
}
