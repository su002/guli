package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.Impl.ClientServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "service-vod" , fallback = ClientServiceImpl.class)  //添加调用的微服务模块
@Component
public interface ClientService {


    @DeleteMapping("/eduvod/video/removeAlyVideo/{id}")

//   @PathVariable("id") 必须要加id 否则 报错
     R removeVideoById(@PathVariable("id") String id);

    @DeleteMapping("delete-batch")
    public R deleteVideo(@PathVariable("videoList") List<String> videoList);
}
