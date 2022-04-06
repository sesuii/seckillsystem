package com.jayce.seckillsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author YoungSong
 * @since 2022-04-06
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class TradeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *  交易id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     *  交易订单号
     */
    private Long orderId;

    /**
     * 汇款方id
     */
    private Long  remitterId;

    /**
     * 收款方id
     */
    private Long payeeId;

    /**
     * 交易时间
     */
    private Date tradingTime;


}
