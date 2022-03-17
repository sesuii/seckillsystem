package com.jayce.seckillsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayce.seckillsystem.entity.SkUser;

/**
 * SkUserService
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
public interface SkUserService extends IService<SkUser> {
    /**
     * 登录方法
     *
     * @param mobilePhone 手机号码
     * @param password 密码
     */
    boolean login(String mobilePhone, String password);

    void createAccount();
}
