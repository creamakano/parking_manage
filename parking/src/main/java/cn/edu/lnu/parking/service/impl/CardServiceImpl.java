package cn.edu.lnu.parking.service.impl;

import cn.edu.lnu.parking.entity.*;
import cn.edu.lnu.parking.mapper.AreaMapper;
import cn.edu.lnu.parking.mapper.CardMapper;
import cn.edu.lnu.parking.service.*;
import cn.edu.lnu.parking.util.BaseVo;
import cn.edu.lnu.parking.util.OrderUtil;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements CardService {

    @Autowired
    private CarNumService carNumService;
    @Autowired
    private UserService userService;
    @Autowired
    private PaymentService paymentService;

    @Override
    public Result insert(CardVo vo) {
        Order order = new Order();
        LambdaQueryWrapper<Card> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Card::getCarNum,vo.getCarNum());
        long days = 30L;
        Integer cost = 280;
        if(vo.getType() == 1){
            days = 365L;
            cost = 2888;
        }
        Card card = this.getOne(wrapper);
        Date now = new Date();
        if(ObjectUtils.isNotNull(card)){
            Date endTime = card.getEndTime();
            if(endTime.getTime()< now.getTime()){
                endTime = now;
            }
            Long endTimeValue = endTime.getTime()+days*86400000L;
            Date endDate = new Date(endTimeValue);
            card.setEndTime(endDate);
            this.updateById(card);
        }else {
            card = new Card();
            card.setCarNum(vo.getCarNum());
            card.setEndTime(new Date(now.getTime() +days*86400000L));
            this.save(card);
        }
        CarNum carNum = carNumService.getByNum(vo.getCarNum());
        if(ObjectUtils.isNotEmpty(carNum)){
            User user = userService.getById(carNum.getUserId());
            user.setPoint(user.getPoint()+cost);
            userService.updateById(user);
        }
        order.setReturnUrl(vo.getReturnUrl());
        order.setSubject("优惠套餐购买");
        order.setTotal_amount(cost.toString());
        order.setOut_trade_no(OrderUtil.getOrderNum());
        return paymentService.toAlipay(order);
    }

    @Override
    public Result checkCarNum(CardVo vo) {
        boolean existCarNum = carNumService.existCarNum(vo.getCarNum(), vo.getUserId());
        return Result.success(existCarNum);
    }

    @Override
    public Result getPage(CardVo vo) {
        Page<CardVo> page = new Page<>(vo.getPageNo(),vo.getPageSize());
        IPage<CardVo> iPage =  baseMapper.getPage(page,vo);
        return Result.success(iPage);
    }

    @Override
    public boolean existCarNum(String carNum) {
        LambdaQueryWrapper<Card> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Card::getCarNum,carNum);
        wrapper.ge(Card::getEndTime,new Date());
        return ObjectUtils.isNotEmpty(this.getOne(wrapper));
    }
}
