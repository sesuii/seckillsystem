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

    boolean reduce(UserFinancial userFinancial, BigDecimal pay);
}
