package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.TPayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-06-16
 */
public interface TPayLogService extends IService<TPayLog> {

    Map payCourseInfo(String orderId);

    //        根据订单id 查询订单支付状态 ,
//        微信支付接口是返回的一个map 集合 , 根据集合获取支付的状态
    Map<String, String> queryPayStatus(String orderId);

    //        如果支付成功 , 则添加数据到订单表  , 修改订单的支付状态
    void saveOrderStatus(Map<String, String> map);
}
