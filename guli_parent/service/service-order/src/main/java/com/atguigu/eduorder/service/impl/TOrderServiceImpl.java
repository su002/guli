package com.atguigu.eduorder.service.impl;

import com.atguigu.commonutils.resultCommont.UcenterMemberOrder;
import com.atguigu.commonutils.resultCommont.courseWebVo;
import com.atguigu.eduorder.client.EduServiceClient;
import com.atguigu.eduorder.client.UcenterServiceClient;
import com.atguigu.eduorder.entity.TOrder;
import com.atguigu.eduorder.mapper.TOrderMapper;
import com.atguigu.eduorder.service.TOrderService;
import com.atguigu.eduorder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-16
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {
    @Autowired
    EduServiceClient eduServiceClient;
    @Autowired
    UcenterServiceClient ucenterServiceClient;


//    生成订单
    @Override
    public String createOrder(String courseId, String memberId) {
//        获取课程信息
        courseWebVo courseInfoOrder = eduServiceClient.getCourseInfo(courseId);

//        获取用户信息
        UcenterMemberOrder userInfoOrder = ucenterServiceClient.getUser(memberId);

//      实现订单插入数据库
        TOrder order = new TOrder();
        String orderNo = OrderNoUtil.getOrderNo();

        //创建Order对象，向order对象里面设置需要数据
        order.setOrderNo(orderNo);//订单号
        order.setCourseId(courseId); //课程id
        order.setCourseTitle(courseInfoOrder.getTitle());
        order.setCourseCover(courseInfoOrder.getCover());
        order.setTeacherName(courseInfoOrder.getTeacherName());
        order.setTotalFee(courseInfoOrder.getPrice());
        order.setMemberId(memberId);
        order.setMobile(userInfoOrder.getMobile());
        order.setNickname(userInfoOrder.getNickname());
        order.setStatus(0);  //订单状态（0：未支付 1：已支付）
        order.setPayType(1);  //支付类型 ，微信1


        baseMapper.insert(order);
        //返回订单号
        return orderNo;
    }
}
