package com.atguigu.edusta.client.Impl;

import com.atguigu.commonutils.R;
import com.atguigu.edusta.client.UcenterClient;
import org.springframework.stereotype.Component;

@Component
public class UcenterClientImpl implements UcenterClient {

    @Override
    public R getUserByDay(String day) {
        return R.ok().message("获取每日注册数量失败");
    }
}
