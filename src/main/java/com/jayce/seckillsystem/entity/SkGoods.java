package com.jayce.seckillsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 秒杀商品
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@Data
@AllArgsConstructor
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


}
