package com.jayce.seckillsystem.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Description: 状态码以及返回信息的枚举值
 * @DATE: 2022/3/24 21:06
 * @Author: YoungSong
 * @File: seckillsystem RestBeanEnum
 * @Email: sjiahui27@gmail.com
 **/

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResultEnum {

    // 通用状态码及信息
    SUCCESS(200, "SUCCESS"),
    FAILED(304, "服务端异常"),

    // 用户验证时状态码及信息
    GET_USER_NOT_FOUND(5001, "用户不存在 请先注册"),
    USER_PASSWORD_ERROR(5002, "用户密码错误"),
    SAVE_USER_REUSE(5003, "用户已存在"),
    SAVE_USER_ERROR(5004, "注册用户失败"),
    AUTH_DENY(401, "用户权限不足 请先登录再操作"),

    // 商品秒杀时状态码及信息
    GET_GOODS_NOT_FOUND(5101, "尚无秒杀商品"),
    GET_GOODS_IS_OVER(5102, "秒杀商品已售罄"),
    GET_GOODS_IS_REUSE(5103, "不得对同一商品重复秒杀"),
    GET_GOODS_IS_NOT_START(5104, "秒杀尚未开始"),
    WITHOUT_ACCESS_AUTHORITY(5105, "当前用户无购买资格"),

    // 生成订单时状态码及信息
    ADD_ORDER_ERROR(5201, "生成订单失败"),
    ORDER_NOT_EXIST(5202, "订单不存在"),
    GET_ORDER_ERROR(5203, "查询订单失败"),
    ORDER_NOT_PAY(5204, "订单仍未支付"),
    ORDER_HAS_CANCEL(5205, "订单已取消"),
    ;

    private int code;
    private String message;
}

