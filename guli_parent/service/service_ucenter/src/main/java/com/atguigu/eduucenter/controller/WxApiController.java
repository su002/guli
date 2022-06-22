package com.atguigu.eduucenter.controller;

import com.atguigu.commonutils.jwtUtils.JwtUtils;
import com.atguigu.eduucenter.entity.UcenterMember;
import com.atguigu.eduucenter.service.UcenterMemberService;
import com.atguigu.eduucenter.utils.HttpClientUtils;
import com.atguigu.eduucenter.utils.WxUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/api/ucenter/wx")
public class WxApiController {
    @Autowired
    private UcenterMemberService memberService;



//微信扫码登录
    @RequestMapping("callback")
    public String callback(String code ,String state) {
//    1.     获取code 的值 ，临时票据，类似验证码


//    2.    拿着code 请求 ，访问微信的固定地址，得到两个值 accsess_token,与open_id
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        //拼接三个参数 ：id  秘钥 和 code值
        String accessTokenUrl = String.format(
                baseAccessTokenUrl,
                WxUtils.APPID,
                WxUtils.APPSECRET,
                code
        );

//     对拼接的地址进行访问, 获取到 accsess_token 和 open_id
//          使用 Httpclient 发送请求得到返回值
        try{
            String accsessToken = HttpClientUtils.get(accessTokenUrl);
            System.out.println("----------------:"+accsessToken);
//            使用Gson 对  accsessToken 转换为 map类型
            Gson gson = new Gson();
            Map tokenAndId = gson.fromJson(accsessToken, HashMap.class);
            String token = (String) tokenAndId.get("access_token");
            String openId = (String) tokenAndId.get("openid");

//          根据openId 查询数据库是否已经存在该用户
            UcenterMember member = memberService.selectByOpenId(openId);


//  第三步:
            if (member == null) {
                // 1 拿着得到accsess_token 和 openid，再去请求微信提供固定的地址，获取到扫描人信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //拼接两个参数
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        token,
                        openId
                );

//         发送请求 , 在该处就能获取到扫码的人的信息
                String userInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println(userInfo);

                HashMap hashMap = gson.fromJson(userInfo, HashMap.class);
//            获取扫码人的头像 与网名
                String nickname =(String) hashMap.get("nickname");
                System.out.println(nickname);
                String headimgurl =(String) hashMap.get("headimgurl");
                System.out.println(headimgurl);
//                为null 做插入操作
                member = new UcenterMember();
                member.setNickname(nickname);   //网名
                member.setOpenid(openId);     //id  唯一标识符
                member.setAvatar(headimgurl); //头像
                memberService.insertWxUser(member);
            }

//      cookie 不能实现跨域
//            解决方案 : 使用jwt 根据对象上传token字符串
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            System.out.println(jwtToken);
//            将 token 字符串 添加到路径上

            return "redirect:http://localhost:3000?token="+jwtToken;

        }catch (Exception e) {
           throw new GuliException(20001,"登录失败");
        }

    }



//    微信扫码登录 的二维码的获取
    @GetMapping("login")
    public String getWxApi(){

//      微信开放平台授权 baseUrl %s相当于 ? 占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

//        做字符编码设置
        String redirecturl = WxUtils.REDIRECTURL;
        try {
//            对地址进行编码设置 , 防止特殊符号
             redirecturl = URLEncoder.encode(redirecturl, "utf-8");
            System.out.println("1111"+redirecturl);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//      使用format 方法将占位符进行填充
        String url = String.format(
                baseUrl,
                WxUtils.APPID,
                redirecturl,
                "atguigu"
        );

        return "redirect:"+url;
    }

}
