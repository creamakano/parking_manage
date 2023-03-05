package cn.edu.lnu.parking.controller;

import cn.edu.lnu.parking.config.AlipayConfig;
import cn.edu.lnu.parking.entity.Order;
import cn.edu.lnu.parking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("pay")
public class PayController {
    @Autowired
    private PaymentService paymentService;
    // @PostMapping("alipay")
    // public String toAlipay(@RequestBody Order alipayBean)  {
    //     String result = paymentService.toAlipay(alipayBean);
    //     return result;
    // }
}
