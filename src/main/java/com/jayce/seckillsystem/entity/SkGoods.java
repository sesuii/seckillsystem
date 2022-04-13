package com.jayce.seckillsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 秒杀产品表
 * </p>
 *
 * @author Gerry
 * @since 2022-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SkGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 秒杀价格
     */
    private BigDecimal skPrice;

    /**
     * 秒杀数量
     */
    private Integer stock;

    /**
     * 秒杀开始时间
     */
    private Date startDateTime;

    /**
     *  秒杀结束时间
     */
    private Date endDateTime;

    /**
     *  准入规则id
     */
    private Long limitedRuleId;

    /**
     * 是否需要预约 默认为0
     */
    private Integer  subscribe;


}
