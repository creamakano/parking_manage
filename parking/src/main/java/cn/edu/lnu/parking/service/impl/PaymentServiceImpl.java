package cn.edu.lnu.parking.service.impl;

import cn.edu.lnu.parking.config.AlipayConfig;
import cn.edu.lnu.parking.entity.Order;
import cn.edu.lnu.parking.service.PaymentService;
import cn.edu.lnu.parking.util.Result;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public Result toAlipay(Order alipayBean)  {
        AlipayClient alipayClient = new DefaultAlipayClient(
                AlipayConfig.gatewayUrl,
                AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json",
                AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);//签名方式
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //这里设置支付后跳转的地址
        alipayRequest.setReturnUrl("http://localhost:7777"+alipayBean.getReturnUrl());
        String out_trade_no = alipayBean.getOut_trade_no();
        String total_amount = alipayBean.getTotal_amount();
        String subject = alipayBean.getSubject();
        String body =null;
        String timeout_express = "5m";
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"timeout_express\":\""+ timeout_express +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String form = null;
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return Result.success(form) ;

    }

    @Override
    public int getLevel(int point) {
        if(point >=10000){
            return 4;
        }else if(point>=5000){
            return 3;
        }else if(point >= 500){
            return 2;
        } else {
            return 1;
        }
    }
}



