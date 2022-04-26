package com.jayce.seckillsystem.entity;

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
 * @since 2022-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BankAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 银行账户名
     */
    private String accountName;

    /**
     * 账户余额
     */
    private BigDecimal accountBalance;


}
