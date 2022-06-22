package com.atguigu.edusta;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient  //开启注册进入nacos  ，用于远程调用
@SpringBootApplication
@EnableFeignClients     // 做服务发现
//扫描所以组件,包括其他模块
@ComponentScan(basePackages = {"com.atguigu"})
@MapperScan("com.atguigu.edusta.mapper")

//开启定时任务
@EnableScheduling
public class staApplication {

    public static void main(String[] args) {
        SpringApplication.run(staApplication.class,args);
    }

}
