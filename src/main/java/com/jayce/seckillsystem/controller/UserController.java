package com.jayce.seckillsystem.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jayce.seckillsystem.constant.RestBeanEnum;
import com.jayce.seckillsystem.entity.User;
import com.jayce.seckillsystem.entity.resp.RestBean;
import com.jayce.seckillsystem.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YoungSong
 * @since 2022-03-23
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户信息操作接口", value = "获取用户信息，用户修改密码等")
@Slf4j
public class UserController {

    @Resource
    private IUserService userService;

    /**
    * @Description 获取用户信息
    *
    * @Param []
    * @return
    *
    * @Author YoungSong
    **/
    @GetMapping("/user-info")
    public RestBean<?> info() {
        SecurityContext context = SecurityContextHolder.getContext();
        User user = userService.getOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getMobilePhone, context.getAuthentication().getName())
        );
        user.setPwd(null);
        return RestBean.success(user);
    }

}
