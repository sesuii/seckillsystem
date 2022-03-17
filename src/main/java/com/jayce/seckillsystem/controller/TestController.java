package com.jayce.seckillsystem.controller;

import com.jayce.seckillsystem.entity.SkUser;
import com.jayce.seckillsystem.service.SkUserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试类
 *
 * @author <a href="mailto:su_1999@126.com">sujian</a>
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    private RestTemplate restTemplate;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private SkUserService skUserService;

    @GetMapping("/user/{id}")
    public void te(@PathVariable("id") Integer id) {
        Long aaa = redisTemplate.opsForValue().increment("aaa");
        System.out.println(aaa);
    }

//    @GetMapping("/produceUserList")
//    public void produceUserList() {
//        List<SkUser> userList = new ArrayList<>(4000);
//        for (int i = 0; i < 4000; i++) {
//            userList.add(new SkUser(i, "user" + i, "123"));
//        }
//        skUserService.saveBatch(userList);
//        String url = "http://localhost:80/toDetail";
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(2);
//        params.add("username", "zs");
//        params.add("password", "123");
//        String result = restTemplate.postForObject(url, params, String.class);
//    }
}
