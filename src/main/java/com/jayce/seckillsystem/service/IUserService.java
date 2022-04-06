package com.jayce.seckillsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayce.seckillsystem.entity.User;
import com.jayce.seckillsystem.entity.resp.RestBean;
import com.jayce.seckillsystem.entity.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YoungSong
 * @since 2022-03-23
 */
public interface IUserService extends IService<User> {

    RestBean<?> createAccount(String username, String identityId, String mobilePhone, String password);

    RestBean<?> doLogin(UserVo userVo);
}
