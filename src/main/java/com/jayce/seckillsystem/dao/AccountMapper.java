package com.jayce.seckillsystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayce.seckillsystem.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountMapper extends BaseMapper<Account> {
}
