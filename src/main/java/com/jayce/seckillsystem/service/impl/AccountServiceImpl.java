package com.jayce.seckillsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayce.seckillsystem.dao.AccountMapper;
import com.jayce.seckillsystem.entity.Account;
import com.jayce.seckillsystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public void createAccount(String username, String identityId, String mobilePhone, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setIdentityId(identityId);
        account.setMobilePhone(mobilePhone);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        account.setPassword(encoder.encode(password));
        accountMapper.insert(account);
    }

    @Override
    public boolean login(String mobilePhone, String password) {
        Account account = getOne(
                new LambdaQueryWrapper<Account>()
                        .eq(Account::getMobilePhone, mobilePhone)
                        .eq(Account::getPassword, password)
        );
        if(account == null) return false;
        return true;
    }

}
