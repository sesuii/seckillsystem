package com.jayce.seckillsystem.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理器
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public void exceptionHandler(Exception e) {
        if (e instanceof DuplicateKeyException) {
            log.error("用户重复对同一件商品下单");
        } else {
            log.error("{}", e.getMessage());
        }
    }
}
