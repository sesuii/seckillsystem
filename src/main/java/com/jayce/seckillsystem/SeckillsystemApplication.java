package com.jayce.seckillsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 */
@SpringBootApplication
@MapperScan("com.jayce.seckillsystem.dao")
public class SeckillsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillsystemApplication.class, args);
    }

}
