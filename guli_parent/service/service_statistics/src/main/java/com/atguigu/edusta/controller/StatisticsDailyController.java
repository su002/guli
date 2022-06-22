package com.atguigu.edusta.controller;


import com.atguigu.commonutils.R;
import com.atguigu.edusta.client.UcenterClient;
import com.atguigu.edusta.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-18
 */
@RestController
@RequestMapping("/edusta/daily")
@CrossOrigin
public class StatisticsDailyController {
    @Autowired
    private StatisticsDailyService statisService;


//    获取当天用户注册的数量
    @GetMapping("getRegisterUser/{day}")
    public R  getUserByDay(@PathVariable("day") String day){

        statisService.getUserByDay(day);

        return R.ok();

    }


//    根据条件查询
//        1. 根据创建日期查询    2 . 根据注册的数量 , 评论数等做查询

//    开始日期, 结束日期
    @GetMapping("selectShow/{type}/{begin}/{end}")
    public R selectShow(@PathVariable(required = false) String type,
                        @PathVariable(required = false) String begin , @PathVariable(required = false) String end){

//            查询 ,使用map类型作为返回结果  , 便于前端获取值
        Map<String ,Object> map = statisService.selectShow(type, begin, end);

        return R.ok().data(map);
    }

}

