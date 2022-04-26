package com.jayce.seckillsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayce.seckillsystem.dao.UserFinancialMapper;
import com.jayce.seckillsystem.entity.UserFinancial;
import com.jayce.seckillsystem.service.IUserFinancialService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Gerry
 * @since 2022-04-06
 */
@Service
public class UserFinancialServiceImpl extends ServiceImpl<UserFinancialMapper, UserFinancial> implements IUserFinancialService {

    @Resource
    UserFinancialMapper userFinancialMapper;

    @Override
    public boolean reduce(UserFinancial userFinancial, BigDecimal pay) {
        return userFinancialMapper.reduce(userFinancial.getId(), pay) == 1;
    }
}
