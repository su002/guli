package com.atguigu.eduorder.client;

import com.atguigu.commonutils.resultCommont.courseWebVo;
import com.atguigu.eduorder.client.Impl.EduServiceClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient(value = "service-edu" ,fallback = EduServiceClientImpl.class)
public interface EduServiceClient {

    /**
     *     根据课程 id 查询 讲师 ,课程相关信息
     * @param id
     * @return
     */

    @RequestMapping("/eduservice/coursefront/getCourseInfo/{id}")
    public courseWebVo getCourseInfo(@PathVariable String id);
}
