package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.jwtUtils.JwtUtils;
import com.atguigu.eduorder.entity.TOrder;
import com.atguigu.eduorder.service.TOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-16
 */
@RestController
@RequestMapping("/eduorder/order")
@CrossOrigin
public class TOrderController {

    @Autowired
    private TOrderService orderService;



//  生成订单
    @PostMapping("/createOrder/{courseId}")
    public R createOrder(@PathVariable String courseId ,
                         HttpServletRequest request){
//        JwtUtils.getMemberIdByJwtToken(request)  用于获取用户的 id
        String userId = JwtUtils.getMemberIdByJwtToken(request);

//        获取订单号
        String order = orderService.createOrder(courseId, userId);
        return R.ok().data("orderId",order);
    }


//    根据订单id 查询订单详情
    @GetMapping("/getOrderInfo/{orderId}")
    public R getOrderInfoger(@PathVariable("orderId") String orderId){
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no" ,orderId);
        TOrder order = orderService.getOne(wrapper);

        return R.ok().data("item",order);
    }
}

