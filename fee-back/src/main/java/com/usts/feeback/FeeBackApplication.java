package com.usts.feeback;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author zdaneel
 */
@MapperScan("com.usts.feeback.dao")
@SpringBootApplication
public class FeeBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeeBackApplication.class, args);
    }

}
