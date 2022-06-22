package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.TOrder;
import com.atguigu.eduorder.service.TPayLogService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-16
 */
@CrossOrigin
@RestController
@RequestMapping("/eduorder/paylog")
public class TPayLogController {
    @Autowired
    private TPayLogService payService;


//      生成微信支付二维码的接口
//       条件是 订单id

    @GetMapping ("/createNative/{orderNo}")
    public R  payCourseInfoByOrderId(@PathVariable String orderNo){
//
        Map map = payService.payCourseInfo(orderNo);

        return R.ok().data("map",map);

    }


    /**    根据订单号 查询支付状态
     *
     */

    @GetMapping("/queryPayStatus/{orderNo}")
    public  R  queryOrderById(@PathVariable String orderNo){

//        根据订单id 查询订单支付状态 ,
//        微信支付接口是返回的一个map 集合 , 根据集合获取支付的状态
        Map<String ,String> map = payService.queryPayStatus(orderNo);

        if (map.isEmpty()){
            throw  new GuliException(400,"支付出错了");
        }
//        判断集合的 的 key 对应的是否为SUCCESS
        if (map.get("trade_state").equals("SUCCESS")){

//        如果支付成功 , 则添加数据到订单表  , 修改订单的支付状态
            payService.saveOrderStatus(map);

            return R.ok().message("支付成功");
        }

        return R.ok().code(25000).message("支付中...");

    }

}

