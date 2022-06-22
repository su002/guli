package com.atguigu.eduorder.utils;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Data
public class WxApiUtil implements InitializingBean {
    @Value("${weixin.pay.addip}")
    private String addIp;

    @Value("${weixin.pay.partner}")
    private String partner;
    @Value("${weixin.pay.partnerkey}")
    private String partnerKey;
    @Value("${weixin.pay.notifyurl}")
    private String notifyurl;

    public static String ADDIP;
    public static String PARTNER;
    public static String PARTNERKEY;
    public static String NOTIFYURL;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.addIp = ADDIP;
        this.partner = PARTNER;
        this.partnerKey = PARTNERKEY;
        this.notifyurl = NOTIFYURL;
    }
}
