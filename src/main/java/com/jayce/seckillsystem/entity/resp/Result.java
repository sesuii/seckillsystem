package com.jayce.seckillsystem.entity.resp;


import com.jayce.seckillsystem.constant.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result<T> {
    int code;
    String reason;
    T data;

    public Result(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public static Result<?> success() {
        return new Result<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());
    }

    public static Result<?> success(Object obj) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), obj);
    }

    public static Result<?> failed(ResultEnum resultEnum) {
        return new Result<>(resultEnum.getCode(), resultEnum.getMessage());
    }

    public static Result<?> failed(ResultEnum resultEnum, Object obj) {
        return new Result<>(resultEnum.getCode(), resultEnum.getMessage(), obj);
    }

}
