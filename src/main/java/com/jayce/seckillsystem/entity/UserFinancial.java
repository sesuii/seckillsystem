package com.jayce.seckillsystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author Gerry
 * @since 2022-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserFinancial implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "id")
    private Long id;

    /**
     *  银行卡号
     */
    private Long cardNumber;

    /**
     * 账户余额
     */
    private BigDecimal balance;

    /**
     * 诚信指数 正常为0 逾期为负数 良好为正数
     */
    private Integer integrityDegree;

    /**
     * 工作状态 待业为-1 未知为0 稳定为1
     */
    private Integer workStatus;

    /**
     *  逾期次数
     */
    private Integer overCount;

    /**
     * 逾期金额
     */
    private BigDecimal overAmount;


}
