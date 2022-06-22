package com.atguigu.msmservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.jwtUtils.RandomUtil;
import com.atguigu.msmservice.service.MsmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
@RequestMapping("/edumsm/msm")
public class MsmController {
    @Autowired
    private MsmService msmService;
    @Autowired
    private RedisTemplate<String ,String> redisTemplate; // 将验证码存入redis , 做验证码超时失效

//    短信发送服务
    @GetMapping("send/{phone}")
    public R Sendmessage(@PathVariable("phone") String phone){

//       从redis 获取电话号码 对应的验证码
        String code = redisTemplate.opsForValue().get(phone);
//        不为null
        if (!StringUtils.isEmpty(code)) {
            return R.ok();
        }

//        如果redis 没有验证码, 则在发送验证码
//        设置短信验证码的位数
        code = RandomUtil.getFourBitRandom();
        Map<String , Object> map = new HashMap<>();

//        调用方法发送短信
        map.put("code",code);

//        将 code 验证码发送到手机号
        boolean flg = msmService.send(map,phone);
//        发送成功
        if (flg == true) {
//            发送成功, 将短信验证码, 与手机号存入redis , 并设置5分钟失效
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.ok();
        }else {
            return R.error().message("短信验证码发送失败");
        }


    }

}
