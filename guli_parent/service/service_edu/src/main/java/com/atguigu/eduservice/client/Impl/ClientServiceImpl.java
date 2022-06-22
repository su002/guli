package com.atguigu.eduservice.client.Impl;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.ClientService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientServiceImpl implements ClientService {
    @Override
    public R removeVideoById(String id) {
        return R.error().message("删除一个视频失败");
    }

    @Override
    public R  deleteVideo(List videoList) {
        return R.error().message("删除多个视频失败");
    }
}
