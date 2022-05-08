package com.jayce.seckillsystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayce.seckillsystem.entity.UserFinancial;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author YoungSong
 * @since 2022-04-06
 */
public interface UserFinancialMapper extends BaseMapper<UserFinancial> {

    /**
    * 减少用户账户金额
    *
    * @param id 用户 ID
    * @param pay 用户需支付的金额
    * @return
    *
    **/
    @Update("update user_financial set balance = balance - #{pay} where id = #{id}")
    int reduce(@Param("id") Long id, @Param("pay") BigDecimal pay);
}
