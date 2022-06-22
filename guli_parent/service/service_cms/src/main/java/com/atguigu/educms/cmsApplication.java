package com.atguigu.educms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.atguigu"})
@SpringBootApplication
@MapperScan(basePackages = {"com.atguigu.educms.mapper"})
public class cmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(cmsApplication.class,args);
    }
}
