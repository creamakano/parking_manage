package cn.edu.lnu.parking.service.impl;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import cn.edu.lnu.parking.entity.*;
import cn.edu.lnu.parking.mapper.CarNumMapper;
import cn.edu.lnu.parking.mapper.UserMapper;
import cn.edu.lnu.parking.service.CarNumService;
import cn.edu.lnu.parking.service.ParkingService;
import cn.edu.lnu.parking.service.UserService;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CarNumServiceImpl extends ServiceImpl<CarNumMapper, CarNum> implements CarNumService {

    @Autowired
    @Lazy
    private ParkingService parkingService;
    @Override
    public Result getPage(CarNumVo vo) {
        LambdaQueryWrapper<CarNum> wrapper = new LambdaQueryWrapper<>();
        this.buildCondition(wrapper,vo);
        IPage<CarNum> page = this.page(new Page<>(vo.getPageNo(), vo.getPageSize()),wrapper);
        return Result.success(page);
    }

    @Override
    public Result getList(CarNum carNum) {
        LambdaQueryWrapper<CarNum> wrapper = new LambdaQueryWrapper<>();
        this.buildCondition(wrapper,carNum);
        List<CarNum> carNumList = this.list(wrapper);
        return Result.success(carNumList);
    }

    @Override
    public List<String> getNumList(Integer userId) {
        LambdaQueryWrapper<CarNum> carNumLambdaQueryWrapper = new LambdaQueryWrapper<>();
        carNumLambdaQueryWrapper.eq(CarNum::getUserId,userId);
        List<String> numList = this.list(carNumLambdaQueryWrapper).stream().map(CarNum::getNum).collect(Collectors.toList());
        return numList;
    }

    @Override
    public Result ListWithoutBuy(CarNum carNum) {
        LambdaQueryWrapper<CarNum> wrapper = new LambdaQueryWrapper<>();
        this.buildCondition(wrapper,carNum);
        List<CarNum> carNumList = this.list(wrapper);
        LambdaQueryWrapper<Parking> parkingLambdaQueryWrapper = new LambdaQueryWrapper<>();
        parkingLambdaQueryWrapper.eq(Parking::getUserId,carNum.getUserId())
                .eq(Parking::getIsPrivate,1);
        List<Parking> parkingList = parkingService.list(parkingLambdaQueryWrapper);
        if(ObjectUtils.isNotEmpty(parkingList)){
            Set<String> exitsCarNums = parkingList.stream().map(Parking::getCarNum).collect(Collectors.toSet());
            carNumList = carNumList.stream().filter(e -> !exitsCarNums.contains(e.getNum())).collect(Collectors.toList());
        }
        return Result.success(carNumList);
    }

    @Override
    public boolean existCarNum(String carNum, Integer id) {
        LambdaQueryWrapper<CarNum> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CarNum::getNum,carNum);
        if(id != null){
            wrapper.ne(CarNum::getId,id);
        }
        return ObjectUtils.isNotEmpty(this.list(wrapper));
    }

    @Override
    public CarNum getCarNumDetail(String num, Integer userId) {
        LambdaQueryWrapper<CarNum> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CarNum::getNum,num);
        wrapper.eq(CarNum::getUserId,userId);
        return getOne(wrapper);
    }

    @Override
    public Result updateCarNum(CarNum carNum) {
        if(existCarNum(carNum.getNum(),carNum.getId())){
            return Result.error("该车牌号已存在，不能重复添加");
        }
        this.updateById(carNum);
        return Result.success();
    }

    void buildCondition(LambdaQueryWrapper<CarNum> wrapper, CarNumVo vo) {
        if(vo.getUserId() != null){
            wrapper.eq(CarNum::getUserId,vo.getUserId());
        }
        if(vo.getType() != null){
            wrapper.eq(CarNum::getType,vo.getType());
        }
        if(ObjectUtils.isNotNull(vo.getNum())){
            wrapper.like(CarNum::getNum,vo.getNum());
        }
    }
    void buildCondition(LambdaQueryWrapper<CarNum> wrapper, CarNum vo) {
        if(vo.getUserId() != null){
            wrapper.eq(CarNum::getUserId,vo.getUserId());
        }
        if(vo.getType() != null){
            wrapper.eq(CarNum::getType,vo.getType());
        }
        if(ObjectUtils.isNotNull(vo.getNum())){
            wrapper.like(CarNum::getNum,vo.getNum());
        }
    }

}
