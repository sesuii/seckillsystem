package com.jayce.seckillsystem.controller;


import com.jayce.seckillsystem.constant.RestBeanEnum;
import com.jayce.seckillsystem.entity.User;
import com.jayce.seckillsystem.entity.resp.RestBean;
import com.jayce.seckillsystem.entity.vo.UserVo;
import com.jayce.seckillsystem.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "用户认证接口", value = "包括用户登录、注册操作")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    IUserService userService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public RestBean<?> register(String username,
                                   String identityId,
                                   String mobilePhone,
                                   String password) {
        return  userService.createAccount(username, identityId, mobilePhone, password);
    }

    @ApiOperation("用户登录")
    @PostMapping(value = "/doLogin")
    public RestBean<?> doLogin(UserVo userVo) {
        return userService.doLogin(userVo);
    }

    @ApiOperation("退出登录")
    @GetMapping("/logout-success")
    public RestBean<?> logoutSuccess() {
        return RestBean.success();
    }

    @ApiOperation("未登录拒绝访问")
    @RequestMapping("/access-deny")
    public RestBean<?> accessDeny() {
        return RestBean.failed(RestBeanEnum.AUTH_DENY);
    }

}
