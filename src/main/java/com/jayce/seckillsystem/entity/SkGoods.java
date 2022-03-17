package com.jayce.seckillsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 秒杀商品
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sk_goods")
public class SkGoods implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商品ID
     */
    @TableField(value = "goods_id")
    private Long goodsId;
    /**
     * 价格
     */
    @TableField(value = "sk_price")
    private Double skPrice;
    /**
     * 库存
     */
    @TableField(value = "stock")
    private Integer stock;

    private static final long serialVersionUID = 1L;
    public static SkGoodsBuilder builder() {
        return new SkGoodsBuilder();
    }
}