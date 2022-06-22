package com.atguigu.eduorder.service.impl;

import com.atguigu.eduorder.entity.TOrder;
import com.atguigu.eduorder.entity.TPayLog;
import com.atguigu.eduorder.mapper.TPayLogMapper;
import com.atguigu.eduorder.service.TOrderService;
import com.atguigu.eduorder.service.TPayLogService;
import com.atguigu.eduorder.utils.HttpClient;
import com.atguigu.eduorder.utils.WxApiUtil;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-16
 */
@Service
public class TPayLogServiceImpl extends ServiceImpl<TPayLogMapper, TPayLog> implements TPayLogService {
    @Autowired
    TOrderService orderService;

    @Override
    public Map payCourseInfo(String orderId) {
        try {

//    1 : 根据 订单id获取订单信息
            QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
            wrapper.eq("orderNo", orderId);
            TOrder order = orderService.getOne(wrapper);

//    2 : 使用map 获取订单信息 , 生成支付二维码
            Map m = new HashMap();
            m.put("appid", WxApiUtil.ADDIP);
            m.put("mch_id", WxApiUtil.PARTNER);
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            m.put("body", order.getCourseTitle()); //课程标题
            m.put("out_trade_no", orderId); //订单号
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");
            m.put("spbill_create_ip", "127.0.0.1");
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            m.put("trade_type", "NATIVE");

            //3 发送httpclient请求，传递参数xml格式，微信支付提供的固定的地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

//      设置为xml格式 参数

            client.setXmlParam(WXPayUtil.generateSignedXml(m, WxApiUtil.NOTIFYURL));
            client.setHttps(true);
//            设置提交方式
            client.post();

//            得到发送请求的返回结果
            //返回内容为xml格式
            String xml = client.getContent();

//            把xml文件格式转换为 Map 类型 把map返回
            Map<String, String> xmlmap = WXPayUtil.xmlToMap(xml);

//            最终结果进行封装到 map集合
            Map map = new HashMap<>();

            map.put("out_trade_no", orderId);                   // 获取   orderId
            map.put("course_id", order.getCourseId());          // 获取课程 Id
            map.put("total_fee", order.getTotalFee());          // 获取订单金额
            map.put("result_code", xmlmap.get("result_code"));  //返回二维码操作状态码
            map.put("code_url", xmlmap.get("code_url"));        //二维码地址

            return map;
        } catch (Exception e) {

            throw new GuliException(20001, "生成二维码失败");
        }


    }




//    查询订单支付状态
    @Override
    public Map<String, String> queryPayStatus(String orderId) {
        try {
        Map map = new HashMap<>();
        map.put("appid", WxApiUtil.ADDIP);
        map.put("mch_id", WxApiUtil.PARTNER);
        map.put("nonce_str", WXPayUtil.generateNonceStr());

        HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

//      将请求参数设置为xml类型

            client.setXmlParam(WXPayUtil.generateSignedXml(map ,WxApiUtil.PARTNERKEY));
            client.setHttps(true);
//      发送post请求
            client.post();

//      得到请求返回的内容
            String content = client.getContent();

//            转换为map
            Map<String, String> xmlToMap = WXPayUtil.xmlToMap(content);

            return xmlToMap;

        } catch (Exception e) {
            return null;

        }
    }

    @Override
    public void saveOrderStatus(Map<String, String> map) {
//        从map中获取订单号
        String orderNo = map.get("out_trade_no");
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();

        wrapper.eq("orderNo",orderNo);

        TOrder order = orderService.getOne(wrapper);

        if (order.getStatus() == 1){
            return;
        }

//        未支付, 修改为支付状态
        order.setStatus(1);
        orderService.updateById(order);

//        添加支付记录
        TPayLog payLog = new TPayLog();
        baseMapper.insert(payLog);

    }
}
