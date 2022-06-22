package com.atguigu.eduorder.client.Impl;

import com.atguigu.commonutils.resultCommont.UcenterMemberOrder;
import com.atguigu.eduorder.client.UcenterServiceClient;
import org.springframework.stereotype.Component;

@Component
public class UcenterServiceClientImpl  implements UcenterServiceClient {


    @Override
    public UcenterMemberOrder getUser(String id) {
        System.out.println("获取用户信息失败");
        return null;
    }
}
