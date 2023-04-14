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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.Date;
import java.util.List;
import java.util.WeakHashMap;

@Service
public class ParkingServiceImpl extends ServiceImpl<ParkingMapper, Parking> implements ParkingService {

    @Autowired
    private ParkingLogService logService;
    @Autowired
    private CarNumService carNumService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserService userService;
    @Autowired
    private CardService cardService;
    @Autowired
    private AreaService areaService;
    @Autowired
    @Lazy
    private CarNumPlaceRelService relService;

    @Override
    public Result insert(Parking parking) {
        if (ObjectUtils.isNull( parking.getType(), parking.getAreaId())) {
            return Result.error("请正确填写信息");
        }
        parking.setCode(this.getCode(parking.getAreaId()));
        this.save(parking);
        return Result.success();
    }

    private String getCode(Integer areaId){
        String prefix = areaService.getById(areaId).getName();
        LambdaQueryWrapper<Parking> wrapper= new LambdaQueryWrapper<>();
        wrapper.eq(Parking::getAreaId,areaId);
        Integer count = this.count(wrapper)+1;
        String countStr = count.toString();
        if(count<10){
            countStr = "00"+countStr;
        }else if(count<100){
            countStr = "0"+countStr;
        }else {

        }
        return prefix+ countStr;

    }

    @Override
    public Result delete(Integer id) {
        return Result.success(this.removeById(id));
    }

    @Override
    public Result update(Parking parking) {
        Integer status = this.getById(parking.getId()).getStatus();
        if(status == 1){
            return Result.error("该车位在停中，禁止修改");
        }
        LambdaQueryWrapper<Parking> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Parking::getCode,parking.getCode())
                .ne(Parking::getId,parking.getId());
        if(ObjectUtils.isNotEmpty(this.list(wrapper))){
            return Result.error("该车位编号已存在");
        }
        return Result.success(this.updateById(parking));
    }

    @Override
    public Result pageList(ParkingVo vo) {
        LambdaQueryWrapper<Parking> wrapper = new LambdaQueryWrapper<>();
        this.buildCondition(wrapper, vo);
        IPage<Parking> page = this.page(new Page<>(vo.getPageNo(), vo.getPageSize()), wrapper);
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
        if (ObjectUtils.isNull(parking.getId(), parking.getNum())) {
            return Result.error("请正确填写信息");
        }
        CarNumPlaceRel rel = relService.getByParkingId(parking.getId());
        if(rel!=null&&!rel.getCarNum().equals(parking.getNum())){
            return Result.error("该私人车位不属于该车牌号");
        }
        parking.setCarNum(parking.getNum());
        LambdaQueryWrapper<Parking> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Parking::getCarNum, parking.getCarNum());
        List<Parking> list = this.list(wrapper);
        if (ObjectUtils.isNotEmpty(list)) {
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
        if (log.getCost() > 0) {
            Order order = new Order();
            order.setOut_trade_no(OrderUtil.getOrderNum());
            order.setSubject("停车收费");
            order.setTotal_amount(String.valueOf(log.getCost()));
            order.setReturnUrl(parking.getReturnUrl());
            //获取车辆主人
            LambdaQueryWrapper<CarNum> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CarNum::getNum, park.getCarNum());
            CarNum owner = carNumService.getOne(wrapper);
            if (owner != null) {
                User user = userService.getById(owner.getUserId());
                int point = user.getPoint() + (int) (double) log.getCost();
                user.setPoint(point);
                user.setLevel(paymentService.getLevel(point));
                userService.updateById(user);
            }
            return paymentService.toAlipay(order);
        }
        return new Result(201, "取车成功，无需缴费");
    }


    private double getPrice(ParkingLog log) {
        long time = log.getEndTime().getTime() - log.getStartTime().getTime();
        if (time < 1800000) {//30分钟
            return 0;
        } else if (time < 12600000) {//3.5小时
            return 5;
        } else {
            time -= 12600000;
            double price = 5;
            price += time / 3600000;
            if (time % 3600000 != 0) {
                price += 1;
            }
            return price;
        }

    }

    @Override
    public Result myPage(ParkingVo vo, Integer userId) {
        LambdaQueryWrapper<Parking> wrapper = new LambdaQueryWrapper<>();
        this.buildCondition(wrapper, vo);
        IPage<Parking> page = this.page(new Page<>(vo.getPageNo(), vo.getPageSize()), wrapper);
        return Result.success(page);
    }

    @Override
    public Result buy(Integer id, String num, Integer userId) {
        if(relService.existCarNum(num)){
            return Result.error("该车牌号已有私人车位");
        }
        Parking parking = this.getById(id);
        parking.setUserId(userId);
        parking.setIsPrivate(1);
        this.updateById(parking);
        CarNumPlaceRel rel = new CarNumPlaceRel();
        rel.setCarNum(num);
        rel.setParkingId(id);
        rel.setUserId(userId);
        rel.setIsRent(0);
        rel.setOriginalCarNum(num);
        rel.setUpdateTime(new Date());
        relService.save(rel);
        Order order = new Order();
        order.setOut_trade_no(OrderUtil.getOrderNum());
        order.setSubject("车位购买");

        //修改积分
        User user = userService.getById(userId);
        if (parking.getType() == 1) {
            order.setTotal_amount(String.valueOf(150000));
            int point = user.getPoint() + 150000;
            user.setPoint(point);
            user.setLevel(paymentService.getLevel(point));
        } else {
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
    public Result parkByCarNum(CarNum carNum) {
        String num = carNum.getNum();
        if(this.getByCarNum(num)!=null){
            return Result.error("该车已在停");
        }
        //查询是不是私人车牌号
        Parking parking = new Parking();
        parking.setCarNum(num);
        parking.setStatus(1);
        parking.setStartTime(new Date());
        Integer parkingId = relService.getPrivateParkingId(num);
        if (parkingId != null) {//是私人
            parking.setId(parkingId);
            this.updateById(parking);
            return Result.success();
        }
        LambdaQueryWrapper<Parking> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Parking::getStatus, 0)
                .eq(Parking::getIsPrivate, 0)
                .eq(Parking::getType, carNum.getType());
        List<Parking> list = this.list(wrapper);
        if (ObjectUtils.isEmpty(list)) {
            return Result.error(501, "已无剩余车位");
        }
        parking.setId(list.get(0).getId());
        if (this.updateById(parking)) {
            return Result.success();
        } else {
            return Result.error(502, "停车失败，请稍后重试");
        }
    }

    @Override
    public Result frontPickUp(Parking parking) {
        Integer id = parking.getId();
        Parking park = this.getById(id);
        //车牌号是否停在私人车位
        Integer privateParkingId = relService.getPrivateParkingId(park.getCarNum());
        boolean isPrivateFlag = privateParkingId!=null&&privateParkingId.equals(parking.getId());
        ParkingLog log = new ParkingLog();
        log.setCarNum(park.getCarNum());
        log.setStartTime(park.getStartTime());
        log.setAreaId(park.getAreaId());
        log.setCode(park.getCode());
        log.setType(park.getType());
        log.setEndTime(new Date());
        log.setCost(isPrivateFlag?0:getPrice(log));
        logService.save(log);
        baseMapper.pickUp(id);
        if(isPrivateFlag){
            return new Result(201, "取车成功，私人车位，无需缴费");
        }
        if(cardService.existCarNum(park.getCarNum())){
            return new Result(201, "取车成功，优惠有效期内，无需缴费");
        }
        if (log.getCost() > 0) {
            Order order = new Order();
            order.setOut_trade_no(OrderUtil.getOrderNum());
            order.setSubject("停车收费");
            order.setTotal_amount(String.valueOf(log.getCost()));
            order.setReturnUrl(parking.getReturnUrl());
            //获取车辆主人
            User user = userService.getById(parking.getUserId());
            int point = user.getPoint() + (int) (double) log.getCost();
            user.setPoint(point);
            user.setLevel(paymentService.getLevel(point));
            userService.updateById(user);
            return paymentService.toAlipay(order);
        }
        return new Result(201, "取车成功，停车时间小于30分钟，无需缴费");

    }

    @Override
    public Result myPlace(CarNumPlaceRelVo vo) {
        Page<CarNumPlaceRel> page = new Page<>(vo.getPageNo(), vo.getPageSize());
        IPage<CarNumPlaceRelVo> iPage = baseMapper.getPage(page, vo);
        return Result.success(iPage);
    }

    @Override
    public Parking getByCarNum(String carNum) {
        LambdaQueryWrapper<Parking> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Parking::getCarNum , carNum);
        return this.getOne(wrapper);
    }

    @Override
    public Result backPickUp(Parking parking) {
        Integer id = parking.getId();
        Parking park = this.getById(id);
        //车牌号是否停在私人车位
        Integer privateParkingId = relService.getPrivateParkingId(park.getCarNum());
        boolean isPrivateFlag = privateParkingId!=null&&privateParkingId.equals(parking.getId());
        ParkingLog log = new ParkingLog();
        log.setCarNum(park.getCarNum());
        log.setStartTime(park.getStartTime());
        log.setAreaId(park.getAreaId());
        log.setCode(park.getCode());
        log.setType(park.getType());
        log.setEndTime(new Date());
        log.setCost(isPrivateFlag?0:getPrice(log));
        logService.save(log);
        baseMapper.pickUp(id);
        if(isPrivateFlag){
            return new Result(201, "取车成功，私人车位，无需缴费");
        }
        if (log.getCost() > 0) {
            Order order = new Order();
            order.setOut_trade_no(OrderUtil.getOrderNum());
            order.setSubject("停车收费");
            order.setTotal_amount(String.valueOf(log.getCost()));
            order.setReturnUrl(parking.getReturnUrl());
            //获取车辆主人
            CarNum carNum = carNumService.getByNum(park.getNum());
            if(carNum!=null) {
                User user = userService.getById(carNum.getUserId());
                int point = user.getPoint() + (int) (double) log.getCost();
                user.setPoint(point);
                user.setLevel(paymentService.getLevel(point));
                userService.updateById(user);
            }
            return paymentService.toAlipay(order);
        }
        return new Result(201, "取车成功，停车时间小于30分钟，无需缴费");

    }


    void buildCondition(LambdaQueryWrapper<Parking> wrapper, ParkingVo vo) {
        if (vo.getType() != null) {
            wrapper.eq(Parking::getType, vo.getType());
        }
        if (vo.getAreaId() != null) {
            wrapper.eq(Parking::getAreaId, vo.getAreaId());
        }
        if (ObjectUtils.isNotNull(vo.getCarNum())) {
            wrapper.like(Parking::getCarNum, vo.getCarNum());
        }
        if (ObjectUtils.isNotNull(vo.getCode())) {
            wrapper.like(Parking::getCode, vo.getCode());
        }
        if (ObjectUtils.isNotNull(vo.getStatus())) {
            wrapper.eq(Parking::getStatus, vo.getStatus());
        }
        if (vo.getIsPrivate() != null) {
            wrapper.eq(Parking::getIsPrivate, vo.getIsPrivate());
        }
        wrapper.orderByAsc(Parking::getAreaId).orderByAsc(Parking::getCode);
    }
}
