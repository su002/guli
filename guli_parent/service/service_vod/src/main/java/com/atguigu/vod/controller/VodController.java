package com.atguigu.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.utils.ConstantVodUtils;
import com.atguigu.vod.utils.InitVodCilent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
public class VodController {

    @Autowired
    private VodService vodService;

    @PostMapping("uploadAlyiVideo")
    public R uploadVideo(MultipartFile file) {
        String videoId = vodService.uploadVideoAly(file);

        return R.ok().data("videoId", videoId);
    }


    //   删除小节 同时 ,删除视频
    @DeleteMapping("removeAlyVideo/{id}")
    public R removeVideoById(@PathVariable String id) {
        try {
            //初始化对象
            DefaultAcsClient client =InitVodCilent.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向request设置视频id
            request.setVideoIds(id);
            //调用初始化对象的方法实现删除
            client.getAcsResponse(request);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001, "删除视频失败");
        }
    }

    //    删除章节时删除小节, 与多个视频,
    @DeleteMapping("delete-batch")
    public R deleteVideo(@PathVariable("videoList") List<String> videoList) {
        vodService.deleteVideo(videoList);

        return R.ok();
    }



//    视频点播  ,点击章节 获取视频的id  实现视频播放

    @GetMapping("getPlayAuth/{id}")
    public R getVideoCourseByCourseId(@PathVariable String id){
//        获取 凭证
        try {
            DefaultAcsClient client =
                    InitVodCilent.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);

//            创建获取视频播放的对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();

            //      将id设置到request 中
            request.setVideoId(id);

    //      调用方法获取凭证
            GetVideoPlayAuthResponse acsResponse = client.getAcsResponse(request);
//          将凭证设置到 返回的响应体中
            String playAuth = acsResponse.getRequestId();
            return  R.ok().data("playAuth",playAuth);

        } catch (ClientException e) {
            throw  new GuliException(400,"视频获取失败");
        }
    }

}


