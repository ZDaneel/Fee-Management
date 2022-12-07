package com.usts.feeback;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
