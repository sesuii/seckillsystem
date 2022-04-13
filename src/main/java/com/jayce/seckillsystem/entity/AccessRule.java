package com.jayce.seckillsystem.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 准入规则表
 * </p>
 *
 * @author Gerry
 * @since 2022-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AccessRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *  主键
     */
    private Long id;

    /**
     *  年龄限制 默认为-1无限制
     */
    private Integer ageLimit;

    /**
     * 工作状态限制 默认-1无限制 1为工作稳定
     */
    private Integer workstatusLimit;

    /**
     *  逾期次数限制默认-1无限制
     */
    private Integer overCountLimit;

    /**
     * 逾期总金额限制 默认-1无限制
     */
    private BigDecimal overTotalAmount;

    /**
     * 逾期天数限制
     */
    private Integer overDay;

    /**
     *  创建时间
     */
    private Date updateTime;


}
