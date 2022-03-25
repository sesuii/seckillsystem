package com.jayce.seckillsystem.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Description: 状态码以及返回信息的枚举值
 * @DATA: 2022/3/24 21:06
 * @Author: YoungSong
 * @File: seckillsystem RestBeanEnum
 * @Email: sjiahui27@gmail.com
 **/

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum RestBeanEnum {

    // 通用状态码及信息
    SUCCESS(200, "SUCCESS"),
    FAILED(304, "服务端异常"),

    // 用户验证时状态码及信息
    GET_USER_NOT_FOUND(001, "用户不存在 请先注册"),
    USER_PASSWORD_ERROR(001, "用户密码错误"),
    SAVE_USER_REUSE(001, "用户已存在"),
    SAVE_USER_ERROR(001, "注册用户失败"),
    AUTH_DENY(001, "用户权限不足 请先登录再操作"),

    // 商品秒杀时状态码及信息
    GET_GOODS_NOT_FOUND(002, "尚无秒杀商品"),
    GET_GOODS_IS_OVER(002, "秒杀商品已售罄"),
    GET_GOODS_IS_REUSE(002, "不得对同一商品重复秒杀"),
    GET_GOODS_IS_NOT_START(002, "秒杀尚未开始"),

    // 生成订单时状态码及信息
    ADD_ORDER_ERROR(003, "生成订单失败"),
    ORDER_NOT_EXIST(003, "订单不存在"),
    GET_ORDER_ERROR(003, "查询订单失败"),
    ;

    private int code;
    private String message;
}

