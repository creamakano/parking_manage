package cn.edu.lnu.parking.service.impl;

import cn.edu.lnu.parking.entity.CarNumPlaceRel;
import cn.edu.lnu.parking.mapper.CarNumPlaceRelMapper;
import cn.edu.lnu.parking.service.CarNumPlaceRelService;
import cn.edu.lnu.parking.service.ParkingService;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CarNumPlaceRelServiceImpl extends ServiceImpl<CarNumPlaceRelMapper, CarNumPlaceRel> implements CarNumPlaceRelService {

    @Autowired
    private ParkingService parkingService;

    @Override
    public boolean existCarNum(String carNum) {
        LambdaQueryWrapper<CarNumPlaceRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CarNumPlaceRel::getCarNum,carNum);
        return ObjectUtils.isNotEmpty(list(wrapper));
    }

    @Override
    public Integer getPrivateParkingId(String carNum) {
        LambdaQueryWrapper<CarNumPlaceRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CarNumPlaceRel::getCarNum,carNum);
        CarNumPlaceRel rel = getOne(wrapper);
        if(rel == null){
            return null;
        }else {
            return rel.getParkingId();
        }

    }

    @Override
    public Result rent(CarNumPlaceRel carNumPlaceRel) {
        CarNumPlaceRel rel = this.getById(carNumPlaceRel.getId());
        if(parkingService.getById(rel.getParkingId()).getStatus() == 1){
            return Result.error("该车位在停中,不能租借");
        }
        //更改时间
        Date updateTime = rel.getUpdateTime();
        Date now = new Date();
        long days = 7 - (now.getTime() - updateTime.getTime()) / 86400000 ;
        if(days > 0 ){
            return Result.error("未达更改时间，距离下次可更改时间还有"+days+"天");
        }
        if(rel.getCarNum().equals(carNumPlaceRel.getCarNum())){
            return Result.error("租借的车牌号和当前车位所属车牌号相同");
        }
        LambdaQueryWrapper<CarNumPlaceRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CarNumPlaceRel::getCarNum,carNumPlaceRel.getCarNum());
        wrapper.ne(CarNumPlaceRel::getId,carNumPlaceRel.getId());
        if(ObjectUtils.isNotEmpty(list(wrapper))){
            return Result.error("租借的车牌号已有专属私人车位");
        }
        carNumPlaceRel.setUpdateTime(now);
        carNumPlaceRel.setIsRent(1);
        this.updateById(carNumPlaceRel);
        return Result.success();
    }

    @Override
    public Result getBack(Integer id) {
        CarNumPlaceRel rel = this.getById(id);
        if(parkingService.getById(rel.getParkingId()).getStatus() == 1){
            return Result.error("该车位在停中,不能更改");
        }
        //更改时间
        Date updateTime = rel.getUpdateTime();
        Date now = new Date();
        long days = 7 - (now.getTime() - updateTime.getTime()) / 86400000 ;
        if(days > 0 ){
            return Result.error("未达更改时间，距离下次可更改时间还有"+days+"天");
        }
        LambdaQueryWrapper<CarNumPlaceRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CarNumPlaceRel::getCarNum,rel.getCarNum());
        wrapper.ne(CarNumPlaceRel::getId,rel.getId());
        if(ObjectUtils.isNotEmpty(list(wrapper))){
            return Result.error("原车牌号已有新的私人车位");
        }
        rel.setUpdateTime(now);
        rel.setIsRent(0);
        rel.setCarNum(rel.getOriginalCarNum());
        this.updateById(rel);
        return Result.success();
    }

    @Override
    public CarNumPlaceRel getByParkingId(Integer parkingId) {
        LambdaQueryWrapper<CarNumPlaceRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CarNumPlaceRel::getParkingId,parkingId);
        return this.getOne(wrapper);
    }
}
