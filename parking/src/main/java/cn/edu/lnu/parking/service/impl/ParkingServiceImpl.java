package cn.edu.lnu.parking.service.impl;

import cn.edu.lnu.parking.entity.*;
import cn.edu.lnu.parking.mapper.ParkingMapper;
import cn.edu.lnu.parking.service.*;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingServiceImpl extends ServiceImpl<ParkingMapper, Parking> implements ParkingService{

    @Autowired
    private ParkingLogService logService;
    @Autowired
    private CarNumService carNumService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserService userService;

    @Override
    public Result insert(Parking parking) {
        if(ObjectUtils.isNull(parking.getCode(),parking.getIsPrivate(),parking.getType(),parking.getAreaId())){
            return Result.error("请正确填写信息");
        }
        this.save(parking);
        return Result.success();
    }

    @Override
    public Result delete(Integer id) {
        return Result.success(this.removeById(id));
    }

    @Override
    public Result update(Parking parking) {
        return Result.success(this.updateById(parking));
    }

    @Override
    public Result pageList(ParkingVo vo) {
        LambdaQueryWrapper<Parking> wrapper = new LambdaQueryWrapper<>();
        this.buildCondition(wrapper,vo);
        IPage<Parking> page = this.page(new Page<>(vo.getPageNo(), vo.getPageSize()),wrapper);
        return Result.success(page);
    }

    @Override
    public Result getList(Parking parking) {
        return Result.success(this.list());
    }

    @Override
    public Result getRemainder() {
        List<RemainderVo> list = baseMapper.getRemainder();
        return Result.success(list);
    }

    @Override
    public Result park(Parking parking) {
        if(ObjectUtils.isNull(parking.getId(),parking.getNum())){
            return Result.error("请正确填写信息");
        }
        parking.setCarNum(parking.getNum());
        LambdaQueryWrapper<Parking> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Parking::getCarNum,parking.getCarNum());
        List<Parking> list = this.list(wrapper);
        if(ObjectUtils.isNotEmpty(list)){
            if(list.get(0).getIsPrivate()==1){
                return Result.error("该车已有专属车位");
            }
            return Result.error("该车已在停");
        }
        Parking park = this.getById(parking.getId());
        park.setId(parking.getId());
        park.setCarNum(parking.getCarNum());
        park.setStartTime(new Date());
        park.setStatus(1);
        this.updateById(park);
        return Result.success();
    }

    @Override
    public Result pickUp(Integer id, Parking parking) {
        Parking park = this.getById(id);
        ParkingLog log = new ParkingLog();
        log.setCarNum(park.getCarNum());
        log.setStartTime(park.getStartTime());
        log.setAreaId(park.getAreaId());
        log.setCode(park.getCode());
        log.setType(park.getType());
        log.setEndTime(new Date());
        log.setCost(getPrice(log));
        logService.save(log);
        baseMapper.pickUp(id);
        if(log.getCost()>0){
            Order order = new Order();
            order.setOut_trade_no(OrderUtil.getOrderNum());
            order.setSubject("停车收费");
            order.setTotal_amount(String.valueOf(log.getCost()));
            order.setReturnUrl(parking.getReturnUrl());
            //获取车辆主人
            LambdaQueryWrapper<CarNum> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CarNum::getNum,park.getCarNum());
            CarNum owner = carNumService.getOne(wrapper);
            if(owner!=null){
                User user = userService.getById(owner.getUserId());
                int point = user.getPoint() + (int) (double) log.getCost();
                user.setPoint(point);
                user.setLevel(paymentService.getLevel(point));
                userService.updateById(user);
            }
            return paymentService.toAlipay(order);
        }
        return new Result(201,"取车成功，无需缴费");
    }



    private double getPrice(ParkingLog log) {
        long time = log.getEndTime().getTime() - log.getStartTime().getTime();
        if(time < 1800000){//30分钟
            return 0;
        }else if(time < 12600000){//3.5小时
            return 5;
        }else {
            time -= 12600000;
            double price = 5;
            price+= time/3600000;
            if(time%3600000!=0){
                price+=1;
            }
            return price;
        }

    }

    @Override
    public Result myPage(ParkingVo vo, Integer userId) {
        LambdaQueryWrapper<Parking> wrapper = new LambdaQueryWrapper<>();
        //获取车牌
        LambdaQueryWrapper<CarNum> carNumLambdaQueryWrapper = new LambdaQueryWrapper<>();
        carNumLambdaQueryWrapper.eq(CarNum::getUserId,userId);
        List<String> numList = carNumService.list(carNumLambdaQueryWrapper).stream().map(CarNum::getNum).collect(Collectors.toList());
        this.buildCondition(wrapper,vo);
        if(ObjectUtils.isNotEmpty(numList)){
            wrapper.and(wr -> wr.or().eq(Parking::getStatus,0).or().in(Parking::getCarNum,numList));
        }else {
            wrapper.eq(Parking::getStatus,0);
        }
        IPage<Parking> page = this.page(new Page<>(vo.getPageNo(), vo.getPageSize()),wrapper);
        return Result.success(page);
    }

    @Override
    public Result buy(Integer id, String num, Integer userId) {
        Parking parking = this.getById(id);
        parking.setUserId(userId);
        parking.setIsPrivate(1);
        parking.setCarNum(num);
        this.updateById(parking);
        Order order = new Order();
        order.setOut_trade_no(OrderUtil.getOrderNum());
        order.setSubject("车位购买");

        //修改积分
        User user = userService.getById(userId);
        if(parking.getType() == 1){
            order.setTotal_amount(String.valueOf(150000));
            int point = user.getPoint() + 150000;
            user.setPoint(point);
            user.setLevel(paymentService.getLevel(point));
        }else {
            order.setTotal_amount(String.valueOf(75000));
            int point = user.getPoint() + 75000;
            user.setPoint(point);
            user.setLevel(paymentService.getLevel(point));
        }
        order.setReturnUrl("/front/home/buyPlace");
        userService.updateById(user);
        return paymentService.toAlipay(order);
    }

    @Override
    public Result myPlace(ParkingVo vo, Integer id) {
        LambdaQueryWrapper<Parking> wrapper = new LambdaQueryWrapper<>();
        Page<Parking> page = new Page<>(vo.getPageNo(), vo.getPageSize());
        wrapper.eq(Parking::getUserId,id);
        vo.setIsPrivate(1);
        buildCondition(wrapper,vo);
        IPage<Parking> iPage = this.page(page, wrapper);
        return Result.success(iPage);

    }

    void buildCondition(LambdaQueryWrapper<Parking> wrapper, ParkingVo vo){
        if(vo.getType() != null){
            wrapper.eq(Parking::getType,vo.getType());
        }
        if(ObjectUtils.isNotNull(vo.getAreaId())){
            wrapper.eq(Parking::getAreaId,vo.getAreaId());
        }
        if(ObjectUtils.isNotNull(vo.getCarNum())){
            wrapper.like(Parking::getCarNum,vo.getCarNum());
        }
        if(ObjectUtils.isNotNull(vo.getCode())){
            wrapper.like(Parking::getCode,vo.getCode());
        }
        if(ObjectUtils.isNotNull(vo.getStatus())){
            wrapper.eq(Parking::getStatus,vo.getStatus());
        }
        if(vo.getIsPrivate()!=null){
            wrapper.eq(Parking::getIsPrivate,vo.getIsPrivate());
        }
    }
}
