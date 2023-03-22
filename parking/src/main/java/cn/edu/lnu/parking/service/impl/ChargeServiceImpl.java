package cn.edu.lnu.parking.service.impl;

import cn.edu.lnu.parking.entity.*;
import cn.edu.lnu.parking.mapper.ChargeMapper;
import cn.edu.lnu.parking.service.*;
import cn.edu.lnu.parking.util.OrderUtil;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ChargeServiceImpl extends ServiceImpl<ChargeMapper, Charge> implements ChargeService {

    @Autowired
    private ChargeLogService logService;
    @Autowired
    private CarNumService carNumService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserService userService;

    @Override
    public Result getPage(ChargeVo vo) {
        if (vo.getPageNo() == null) {
            vo.setPageNo(1);
        }
        if (vo.getPageSize() == null) {
            vo.setPageSize(10);
        }
        LambdaQueryWrapper<Charge> wrapper = new LambdaQueryWrapper<>();
        Charge charge = new Charge();
        BeanUtils.copyProperties(vo, charge);
        buildCondition(charge, wrapper);
        IPage<Charge> page = this.page(new Page<>(vo.getPageNo(), vo.getPageSize()), wrapper);
        return Result.success(page);
    }

    private void buildCondition(Charge charge, LambdaQueryWrapper<Charge> wrapper) {
        if (ObjectUtils.isNotNull(charge.getCarNum())) {
            wrapper.like(Charge::getCarNum, charge.getCarNum());
        }
        if (ObjectUtils.isNotNull(charge.getCode())) {
            wrapper.like(Charge::getCode, charge.getCode());
        }
        if (ObjectUtils.isNotNull(charge.getStatus())) {
            wrapper.eq(Charge::getStatus, charge.getStatus());
        }
        if (ObjectUtils.isNotNull(charge.getType())) {
            wrapper.eq(Charge::getType, charge.getType());
        }
    }
    private void buildCondition(ChargeVo charge, LambdaQueryWrapper<Charge> wrapper) {
        if (ObjectUtils.isNotNull(charge.getCarNum())) {
            wrapper.like(Charge::getCarNum, charge.getCarNum());
        }
        if (ObjectUtils.isNotNull(charge.getCode())) {
            wrapper.like(Charge::getCode, charge.getCode());
        }
        if (ObjectUtils.isNotNull(charge.getStatus())) {
            wrapper.eq(Charge::getStatus, charge.getStatus());
        }
        if (ObjectUtils.isNotNull(charge.getType())) {
            wrapper.eq(Charge::getType, charge.getType());
        }
    }

    @Override
    public Result insert(Charge charge) {
        if (ObjectUtils.isNull(charge.getCode()) || charge.getType() == null) {
            return Result.error("请正确填写");
        }
        charge.setStatus(0);
        LambdaQueryWrapper<Charge> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Charge::getCode, charge.getCode());
        if (ObjectUtils.isNotNull(this.list(wrapper))) {
            return Result.error("编号重复，请重新输入");
        }
        this.save(charge);
        return Result.success();
    }

    @Override
    public Result start(Charge charge) {
        if (ObjectUtils.isNull(charge.getNum()) || charge.getChargeTime() == null) {
            return Result.error("参数错误，请正确填写");
        }
        LambdaQueryWrapper<Charge> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Charge::getCarNum,charge.getNum());
        if(ObjectUtils.isNotEmpty(this.list(wrapper))){
            return Result.error("该车充电中");
        }

        charge.setCarNum(charge.getNum());
        charge.setStartTime(new Date());
        charge.setStatus(1);
        this.updateById(charge);
        return Result.success();
    }

    @Override
    public Result stop(Integer id) {
        Charge charge = this.getById(id);
        Long hour = Long.valueOf(charge.getChargeTime().substring(0, 2));
        Long min = Long.valueOf(charge.getChargeTime().substring(3, 5));
        Date now = new Date();
        Date endTime = new Date(charge.getStartTime().getTime() + hour * 60 * 60 * 1000 + min * 60 * 1000);
        if (now.getTime() < endTime.getTime()) {
            endTime = now;
        }
        ChargeLog log = new ChargeLog();
        log.setStartTime(charge.getStartTime());
        log.setCarNum(charge.getCarNum());
        log.setCode(charge.getCode());
        log.setEndTime(endTime);
        log.setCarNum(charge.getCarNum());
        log.setType(charge.getType());
        log.setCost(getPrice(log));
        logService.save(log);
        baseMapper.stopCharge(id);
        Order order = new Order();
        order.setOut_trade_no(OrderUtil.getOrderNum());
        order.setSubject("充电收费");
        order.setTotal_amount(String.valueOf(log.getCost()));
        order.setReturnUrl("/front/home/charge");
        //获取车辆主人
        LambdaQueryWrapper<CarNum> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CarNum::getNum,log.getCarNum());
        CarNum own = carNumService.getOne(wrapper);
        if(own!=null){
            User user = userService.getById(own.getUserId());
            int point = user.getPoint() + (int) (double) log.getCost();
            user.setLevel(paymentService.getLevel(point));
            user.setPoint(point);
            userService.updateById(user);
        }
        return paymentService.toAlipay(order);
    }

    double getPrice(ChargeLog log){
        long time = log.getEndTime().getTime() - log.getStartTime().getTime();
        long num = 0;
        num = time/900000;
        if(time%900000!=0){
            num++;
        }
        if(log.getType() == 1){
            return num*4;
        }else {
            String str = String.format("%.2f",num*0.2);
            return Double.parseDouble(str);
        }
    }

    @Override
    public Result getFrontPage(ChargeVo vo, Integer userId) {
        LambdaQueryWrapper<Charge> wrapper = new LambdaQueryWrapper<>();
        this.buildCondition(vo,wrapper);
        IPage<Charge> page = this.page(new Page<>(vo.getPageNo(), vo.getPageSize()),wrapper);
        return Result.success(page);
    }

    @Override
    public Result stop(Integer id, Integer userId) {
        Charge charge = this.getById(id);
        Long hour = Long.valueOf(charge.getChargeTime().substring(0, 2));
        Long min = Long.valueOf(charge.getChargeTime().substring(3, 5));
        Date now = new Date();
        Date endTime = new Date(charge.getStartTime().getTime() + hour * 60 * 60 * 1000 + min * 60 * 1000);
        if (now.getTime() < endTime.getTime()) {
            endTime = now;
        }
        ChargeLog log = new ChargeLog();
        log.setStartTime(charge.getStartTime());
        log.setCarNum(charge.getCarNum());
        log.setCode(charge.getCode());
        log.setEndTime(endTime);
        log.setCarNum(charge.getCarNum());
        log.setType(charge.getType());
        log.setCost(getPrice(log));
        logService.save(log);
        baseMapper.stopCharge(id);
        Order order = new Order();
        order.setOut_trade_no(OrderUtil.getOrderNum());
        order.setSubject("充电收费");
        order.setTotal_amount(String.valueOf(log.getCost()));
        order.setReturnUrl("/front/home/charge");
        //获取车辆主人
        User user = userService.getById(userId);
        int point = user.getPoint() + (int) (double) log.getCost();
        user.setLevel(paymentService.getLevel(point));
        user.setPoint(point);
        userService.updateById(user);
        return paymentService.toAlipay(order);
    }
}
