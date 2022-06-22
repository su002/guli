package com.atguigu.eduucenter.service;

import com.atguigu.eduucenter.entity.UcenterMember;
import com.atguigu.eduucenter.entity.vo.RegisterMember;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-06-11
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember member);

    void register(RegisterMember registerMember);


//    根据 openId 查询扫码登录的用户是否存在
    UcenterMember selectByOpenId(String openId);

//    添加微信扫码用户
    void insertWxUser(UcenterMember ucenterMember);

    //    获取当天用户注册的数量
    Integer getUserByDay(String day);
}
