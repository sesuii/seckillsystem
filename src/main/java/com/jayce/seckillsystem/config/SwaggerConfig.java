package com.jayce.seckillsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author gerry
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        ApiInfo info = new ApiInfoBuilder()
                .contact(new Contact("Song", "https://www.google.com", "songjiah27@gmail.com"))
                .title("秒杀系统 - 在线API接口文档")
                .description("秒杀系统的后端API文档，欢迎前端人员查阅！")
                .build();
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(info)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jayce.seckillsystem.controller"))
                .build();
    }

}
