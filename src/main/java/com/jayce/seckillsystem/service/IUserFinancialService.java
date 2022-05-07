package com.jayce.seckillsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayce.seckillsystem.entity.UserFinancial;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YoungSong
 * @since 2022-04-06
 */
public interface IUserFinancialService extends IService<UserFinancial> {

    /**
     * 减少用户账户金额
     *
     * @param userFinancial 用户账户信息
     * @param pay 支付金额
     * @return
     *
     **/
    boolean reduce(UserFinancial userFinancial, BigDecimal pay);
}
