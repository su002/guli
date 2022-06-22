package com.atguigu.eduorder;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient  //开启注册进入nacos
@SpringBootApplication
@EnableFeignClients     // 做服务发现
//扫描所以组件,包括其他模块
@ComponentScan(basePackages = {"com.atguigu"})
//@MapperScan("com.atguigu.eudorder.mapper")
public class orderApplication {

    public static void main(String[] args) {
        SpringApplication.run(orderApplication.class,args);
    }
}
