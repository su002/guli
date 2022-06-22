package com.atguigu.eduucenter.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WxUtils implements InitializingBean {
    public   static  String APPID ;
    public  static  String APPSECRET ;
    public  static  String REDIRECTURL ;

    @Value("${wx.open.app_id}")
    private String appId;
    @Value("${wx.open.app_secret}")
    private String appSecret;
    @Value("${wx.open.redirect_url}")
    private String redirectUrl;

    @Override
    public void afterPropertiesSet() throws Exception {
        APPID = appId;
        APPSECRET = appSecret;
        REDIRECTURL = redirectUrl;
    }
}
