package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.ClientService;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-03
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {
    @Autowired
    EduVideoService eduVideoService;
    @Autowired
    private  ClientService clientService;



//    添加章节下小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return R.ok();
    }
//    删除小节 , 并删除小节的视频
//    TODO
    @PostMapping("{id}")
    public R deleteVideo(@PathVariable String id)
    {
        EduVideo eduVideo = eduVideoService.getById(id);
//        获取video的id
        String videoSourceId = eduVideo.getVideoSourceId();
        if (!videoSourceId.isEmpty()){
            //        让fegin 调用其他模块的方法
           R code =  clientService.removeVideoById(videoSourceId);
           if (code.getCode() == 20001) {
                throw new GuliException(20001,"执行了熔断器");
           }
        }

        eduVideoService.removeById(id);
        return R.ok();
    }



//    修改小节
//    TODO
}

