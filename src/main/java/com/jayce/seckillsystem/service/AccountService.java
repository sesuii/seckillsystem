package com.jayce.seckillsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayce.seckillsystem.entity.Account;
import org.springframework.stereotype.Service;

public interface AccountService extends IService<Account> {


    void createAccount(String username, String identityId, String mobilePhone, String password);

    boolean login(String mobilePhone, String password);
}
