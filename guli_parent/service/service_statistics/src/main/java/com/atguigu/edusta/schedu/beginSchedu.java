package com.atguigu.edusta.schedu;


import com.atguigu.edusta.service.StatisticsDailyService;
import com.atguigu.edusta.utils.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class beginSchedu {
    @Autowired
    private StatisticsDailyService dailyService;

//    每五秒执行一次
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void test(){
//        System.out.println("执行****");
//    }

//    每日凌晨刷新
    @Scheduled(cron = "0 0 1 * * ?")
    public void ScheduledTesk(){

//        使用定时任务 获取昨日用户注册量
//    1 : 获取昨日的日期

        dailyService.getUserByDay(DateUtil.formatDate(DateUtil.addDays(new Date(),-1)));

    }


}
