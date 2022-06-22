package com.atguigu.eduucenter.client;


import com.atguigu.commonutils.R;
import com.atguigu.eduucenter.client.Impl.EduClientServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@FeignClient(value = "service-edu",fallback = EduClientServiceImpl.class)
@Component
public interface EduClientService {

//    调用service-edu 模块 使用fengin
    @GetMapping("getComment/{id}")
    public R getComment(@PathVariable("id") Long id,HttpServletRequest request);


    @GetMapping("/eduservice/course/getCourseInfo/{courseId}")
    public R getCourseById(@PathVariable("courseId") String courseId);
}
