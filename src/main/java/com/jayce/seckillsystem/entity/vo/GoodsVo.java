package com.jayce.seckillsystem.entity.vo;

import com.jayce.seckillsystem.entity.Goods;
import lombok.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 商品详情页——商品信息
 * @DATA: 2022/3/24 22:57
 * @Author: YoungSong
 * @File: seckillsystem GoodsVo
 * @Email: sjiahui27@gmail.com
 **/

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GoodsVo extends Goods {
    /**
     * 秒杀价格
     **/
    private BigDecimal skPrice;

    /**
     * 剩余数量
     **/
    private Integer stock;

    /**
     * 开始时间
     **/
    private Date startDateTime;

    /**
     * 结束时间
     **/
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
