package com.jayce.seckillsystem.entity.vo;

import com.jayce.seckillsystem.entity.OrderInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 订单详情类
 * @DATE: 2022/4/4 23:13
 * @Author: YoungSong
 * @File: seckillsystem OrderInfoVo
 * @Email: sjiahui27@gmail.com
 **/

@Data
@Builder
public class OrderInfoVo {

    OrderInfo orderInfo;

    GoodsVo goodsVo;

}
