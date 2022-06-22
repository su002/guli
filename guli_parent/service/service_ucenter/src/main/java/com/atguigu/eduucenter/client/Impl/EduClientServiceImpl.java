package com.atguigu.eduucenter.client.Impl;

import com.atguigu.commonutils.R;
import com.atguigu.eduucenter.client.EduClientService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class EduClientServiceImpl implements EduClientService {
    @Override
    public R getComment(Long id, HttpServletRequest request) {

        return R.error().message("获取信息失败");
    }

    @Override
    public R getCourseById(String courseId) {
        return R.error().message("获取信息失败");
    }
}
