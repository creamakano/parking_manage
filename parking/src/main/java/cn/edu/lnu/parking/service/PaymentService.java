package cn.edu.lnu.parking.service;

import cn.edu.lnu.parking.entity.Order;
import cn.edu.lnu.parking.util.Result;

public interface PaymentService  {
    Result toAlipay(Order alipayBean);
    int getLevel(int point) ;
}
