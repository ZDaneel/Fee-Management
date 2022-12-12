package com.usts.feeback;

import com.usts.feeback.aop.EnableApiLog;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zdaneel
 */
@EnableApiLog
@MapperScan("com.usts.feeback.dao")
@SpringBootApplication
public class FeeBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeeBackApplication.class, args);
    }

}
