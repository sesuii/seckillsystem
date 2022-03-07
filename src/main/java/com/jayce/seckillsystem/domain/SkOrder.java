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
 * 秒杀订单
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sk_order")
public class SkOrder implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 商品ID
     */
    @TableField(value = "goods_id")
    private Integer goodsId;

    private static final long serialVersionUID = 1L;

    public static SkOrderBuilder builder() {
        return new SkOrderBuilder();
    }
}