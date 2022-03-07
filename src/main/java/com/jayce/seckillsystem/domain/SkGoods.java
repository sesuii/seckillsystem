package com.jayce.seckillsystem.domain;

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
    private Integer id;

    /**
     * 商品名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 价格
     */
    @TableField(value = "price")
    private Integer price;

    /**
     * 库存
     */
    @TableField(value = "stock")
    private Integer stock;

    /**
     * 版本号
     */
    @TableField(value = "`version`")
    private Integer version;

    private static final long serialVersionUID = 1L;

    public static SkGoodsBuilder builder() {
        return new SkGoodsBuilder();
    }
}