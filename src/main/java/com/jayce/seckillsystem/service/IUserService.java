package com.jayce.seckillsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayce.seckillsystem.entity.User;
import com.jayce.seckillsystem.entity.resp.Result;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YoungSong
 * @since 2022-03-23
 */
public interface IUserService extends IService<User> {

    User createAccount(String username, String identityId, String mobilePhone, String password);

    Map<String, String> toLogin(User user);
}
