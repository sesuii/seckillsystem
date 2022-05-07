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

    /**
     * 创建新的用户
     *
     * @param username 用户姓名
     * @param identityId 用户身份证
     * @param mobilePhone 用户手机号
     * @param password 用户密码
     * @return 用户信息
     *
     **/
    User createAccount(String username, String identityId, String mobilePhone, String password);

    /**
     * 用户登录
     *
     * @param user 用户信息
     * @return
     *
     **/
    Map<String, String> toLogin(User user);
}
