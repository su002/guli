package com.atguigu.edusta.service;

import com.atguigu.edusta.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-06-18
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    //    获取当天用户注册的数量
    void getUserByDay(String day);

    //    根据条件查询
    //    开始日期, 结束日期 , 数量等
    Map<String, Object> selectShow(String keyword, String begin, String end);
}
