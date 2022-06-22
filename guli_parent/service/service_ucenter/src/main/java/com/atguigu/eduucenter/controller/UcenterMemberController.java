package com.atguigu.eduucenter.controller;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.jwtUtils.JwtUtils;
import com.atguigu.commonutils.resultCommont.UcenterMemberOrder;
import com.atguigu.eduucenter.entity.UcenterMember;
import com.atguigu.eduucenter.entity.vo.RegisterMember;
import com.atguigu.eduucenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-11
 */

@CrossOrigin
@RestController
@RequestMapping("/educenter/member")
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    /**
     * 登录
     *
     * @param member
     * @return
     */

    @PostMapping("login")
    public R login(@RequestBody UcenterMember member) {
        String token = memberService.login(member);

        return R.ok().data("token", token);
    }


    /**
     * 注册
     *
     * @param registerMember
     * @return
     */
    @PostMapping("register")
    public R register(@RequestBody RegisterMember registerMember) {

        memberService.register(registerMember);
        return R.ok();
    }


    /**
     * 根据token 获取用户信息 , 在页面回显
     *
     * @return
     */
//    根据工具类的方法实现
    @GetMapping("getMemberInfo")
    public R getTokenById(HttpServletRequest request) {
//        调用工具类传入 request 对象 获取用户信息的 id
        String userId = JwtUtils.getMemberIdByJwtToken(request);
//        根据id查询用户信息
        UcenterMember userMember = memberService.getById(userId);

        return R.ok().data("userInfo", userMember);
    }


    /**
     * 支付的接口:
     * 获取用户的id 查询用户信息,
     * 1. 用户信息在其他模块需要使用 , 直接返回
     */
    @GetMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUser(@PathVariable String id) {
        UcenterMember member = memberService.getById(id);
//        返回做一次转换
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();

        BeanUtils.copyProperties(member, ucenterMemberOrder);
        return ucenterMemberOrder;
    }


    /**
     * 退出登录
     */
    @PostMapping("logout")
    public R logout() {

        return R.ok();
    }



    //    获取当天用户注册的数量
    @GetMapping("/getDayUser/{day}")
    public R getUserByDay(@PathVariable("day") String day) {

        Integer count = memberService.getUserByDay(day);

        return R.ok().data("count", count);


    }

}

