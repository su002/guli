package com.atguigu.edusta.client;


import com.atguigu.commonutils.R;
import com.atguigu.edusta.client.Impl.UcenterClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-ucenter" ,fallback = UcenterClientImpl.class)
@Component
public interface UcenterClient {

    @GetMapping("/educenter/member/getDayUser/{day}")
    public R getUserByDay(@PathVariable("day") String day);
}


