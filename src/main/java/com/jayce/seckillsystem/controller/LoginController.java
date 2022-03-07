package com.jayce.seckillsystem.controller;

import com.jayce.seckillsystem.service.SkUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * 登录管理
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@Controller
@Slf4j
public class LoginController {
    @Resource
    private SkUserService skUserService;
//    @GetMapping("/")
    @RequestMapping("/login")
    public String toLogin() {
        return "login";
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
