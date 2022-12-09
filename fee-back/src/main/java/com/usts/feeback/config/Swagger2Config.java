package com.usts.feeback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author leenadz
 * @since 2022-12-09 17:19
 */
@EnableSwagger2
@EnableWebMvc
@Configuration
public class Swagger2Config {


    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.usts.feeback.controller"))
                .build()
                .pathMapping("/")
                .apiInfo(apiInfo());
    }

    /**
     * 接口文档信息设置
     *
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("班费管理系统接口文档")
                .contact(new Contact("邹逸晨", null, null))
                .description("班费管理系统接口文档")
                .version("1.0")
                .build();
    }


}
