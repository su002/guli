package com.atguigu.edusta.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.edusta.client.UcenterClient;
import com.atguigu.edusta.entity.StatisticsDaily;
import com.atguigu.edusta.mapper.StatisticsDailyMapper;
import com.atguigu.edusta.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-18
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {
    @Autowired
    private StatisticsDailyMapper statisMapper;
    @Autowired
    private UcenterClient ucenterClient;


    @Override
    @Transactional
    public void getUserByDay(String day) {
//        再根据查询日期 注册人数时 , 一天不同时间段会造成多条相同日期注册数
//        需要删除之后再做添加
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
//        根据日期查询数据
        wrapper.eq("date_calculated",day);
//        删除
        baseMapper.delete(wrapper);

//        调用edu-service 模块 实现 根据注册日期查询 人数
        R userByDay = ucenterClient.getUserByDay(day);
        Map<String, Object> data = userByDay.getData();
        Integer count =(Integer) data.get("count");

//      获取人数 , 添加数据库

        StatisticsDaily sta = new StatisticsDaily();

//        添加信息
        sta.setCourseNum(count);
        sta.setDateCalculated(day);

        sta.setLoginNum(RandomUtils.nextInt(100,200));
        sta.setCourseNum(RandomUtils.nextInt(100,200));
        sta.setVideoViewNum(RandomUtils.nextInt(100,200));
        statisMapper.insert(sta);
    }



    //    根据条件查询
    //    开始日期, 结束日期 , 数量等
    @Override
    public Map<String, Object> selectShow(String keyword, String begin, String end) {
//            查询
//        封装条件 , 作为查询
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();

        wrapper.between("gmt_create",begin ,end);

        wrapper.eq("date_calculated", keyword);
//        封装该条件查询
        List<StatisticsDaily> dailyList = baseMapper.selectList(wrapper);


        /**
         * 为前端返回值 :  1. 数组形式 对应后端的List集合 ,创建两个集合 , 分别对应
         *                     1.1 :日期查询出的集合
         *                     1.2 : keyword 查询出的集合
         */
//        keyword 对应的数量
        List<Integer> numdataList = new ArrayList<>();
//        日期
        List<String> date_calculatedList = new ArrayList<>();

//        循环
        for(StatisticsDaily daily : dailyList){
//            日期
            String dateCalculated = daily.getDateCalculated();
//            添加到集合
            date_calculatedList.add(dateCalculated);

//            根据数量分类 查询结果添加到集合
            switch (keyword){
                case "register_num":
                    numdataList.add(daily.getRegisterNum());
                case "course_num":
                    numdataList.add(daily.getCourseNum());
                case "video_view_num":
                    numdataList.add(daily.getVideoViewNum());
                case "login_num":
                    numdataList.add(daily.getLoginNum());
                default:
                    break;
            }
        }

//       将其添加到 map 集合
        Map<String, Object> map = new HashMap<>();
        map.put("numDataList",numdataList);
        map.put("date_calculatedList",date_calculatedList);
        return map;
    }
}
