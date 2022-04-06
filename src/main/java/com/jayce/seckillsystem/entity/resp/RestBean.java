package com.jayce.seckillsystem.entity.resp;


import com.jayce.seckillsystem.constant.RestBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.ObjIntConsumer;

@Data
@AllArgsConstructor
public class RestBean<T> {
    int code;
    String reason;
    T data;

    public RestBean(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public static RestBean<?> success() {
        return new RestBean<>(RestBeanEnum.SUCCESS.getCode(), RestBeanEnum.SUCCESS.getMessage());
    }

    public static RestBean<?> success(Object obj) {
        return new RestBean<>(RestBeanEnum.SUCCESS.getCode(), RestBeanEnum.SUCCESS.getMessage(), obj);
    }

    public static RestBean<?> failed(RestBeanEnum restBeanEnum) {
        return new RestBean<>(restBeanEnum.getCode(), restBeanEnum.getMessage());
    }

    public static RestBean<?> failed(RestBeanEnum restBeanEnum, Object obj) {
        return new RestBean<>(restBeanEnum.getCode(), restBeanEnum.getMessage(), obj);
    }

}
