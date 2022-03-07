package com.jayce.seckillsystem.config;

import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 相关配置
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@Configuration
public class MyBatisPlusConfig {
    /**
     * 乐观锁插件配置
     *
     * @return
     */
    @Bean
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }
}
