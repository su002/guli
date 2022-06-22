package com.atguigu.eduucenter.service.impl;

import com.alibaba.nacos.common.util.Md5Utils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.jwtUtils.JwtUtils;
import com.atguigu.commonutils.jwtUtils.MD5;
import com.atguigu.eduucenter.entity.UcenterMember;
import com.atguigu.eduucenter.entity.vo.RegisterMember;
import com.atguigu.eduucenter.mapper.UcenterMemberMapper;
import com.atguigu.eduucenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-11
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
    @Autowired
    private RedisTemplate<String ,String> redisTemplate;
    @Autowired
    UcenterMemberMapper ucenterMemberMapper;

//    登录
    @Override
    public String login(UcenterMember member) {
        String mobile = member.getMobile();
        String password = member.getPassword();
//        判断提交的信息是否为null
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "请输入登录信息");
        }

        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();

        wrapper.eq("mobile" ,mobile);
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);
//        判断
        if (ucenterMember == null) {
            throw new GuliException(20001,"账户名,或密码输入错误");
        }

//        对密码进行加密
        String encrypt = MD5.encrypt(password);
//        判断密码

        if (!ucenterMember.getPassword().equals(encrypt)) {
            throw new GuliException(20001,"账户名,或密码输入错误");
        }
//        判断用户是否被禁用
        if (ucenterMember.getIsDisabled()) {
            throw new GuliException(20001,"账户已冻结,请联系管理员");
        }

//        登录成功 , 设置 token
//        获取 登录用户名的 id 与 登录昵称
        String jwtToken = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());

        return jwtToken;
    }

    @Override
    public void register(RegisterMember registerMember) {
        String code = registerMember.getCode();
        String mobile = registerMember.getMobile();
        String nickname = registerMember.getNickname();
        String password = registerMember.getPassword();

        if (mobile.isEmpty() || code.isEmpty() || nickname.isEmpty() ||password.isEmpty()) {
            throw new GuliException(20001,"请输入手机号");
        }

//        手机号不为null 发送验证码
//        从redis 中获取验证码
        String phoneCode = redisTemplate.opsForValue().get("phone");

//        判断输入验证码与redis的是否一致
        if (!phoneCode.equals(code)){
        throw  new GuliException(20001,"验证码错误或失效");
        }

//      判断是否有重复的手机号
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
//        根据手机号查询信息
        Integer integer = baseMapper.selectCount(wrapper);

        if (integer==1){
            throw new GuliException(20001,"手机号已注册");
        }
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
//        表示当前用户不被禁用
        member.setIsDisabled(false);
        member.setPassword(MD5.encrypt(password));
        //设置默认头像
        member.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKDRfib8wy7A2ltERKh4VygxdjVC1x5OaOb1t9hot4JNt5agwaVLdJLcD9vJCNcxkvQnlvLYIPfrZw/132");
        baseMapper.insert(member);
    }





//    根据id 查询
    @Override
    public UcenterMember selectByOpenId(String openId) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openId);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

//    添加微信扫码登录的用户
    @Override
    public void insertWxUser(UcenterMember ucenterMember) {
        baseMapper.insert(ucenterMember);
    }


    //    获取当天用户注册的数量
    @Override
    public Integer getUserByDay(String day) {

        return ucenterMemberMapper.getUserByDay(day);
    }
}
