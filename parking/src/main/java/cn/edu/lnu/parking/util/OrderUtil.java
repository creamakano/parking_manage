package cn.edu.lnu.parking.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderUtil {
    public static synchronized String getOrderNum(){
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String localDate = LocalDateTime.now().format(ofPattern);
        //3位随机数
        String randomNumeric = RandomStringUtils.randomNumeric(3);
        String orderNum = localDate + randomNumeric;
        return orderNum;
    }
}
