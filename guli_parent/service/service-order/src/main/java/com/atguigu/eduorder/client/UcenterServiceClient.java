package com.atguigu.eduorder.client;

import com.atguigu.commonutils.resultCommont.UcenterMemberOrder;
import com.atguigu.eduorder.client.Impl.UcenterServiceClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "service-ucenter" ,fallback = UcenterServiceClientImpl.class)
public interface UcenterServiceClient {
    /** 支付的接口:
     *      获取用户的id 查询用户信息,
     *          1. 用户信息在其他模块需要使用 , 直接返回
     */
    @GetMapping("/educenter/member/getUserInfoOrder/{id}")
    public UcenterMemberOrder getUser(@PathVariable("id") String id);




}
