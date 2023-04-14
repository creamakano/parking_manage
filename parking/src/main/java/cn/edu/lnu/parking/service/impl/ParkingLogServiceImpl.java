package cn.edu.lnu.parking.service.impl;

import cn.edu.lnu.parking.entity.CarNum;
import cn.edu.lnu.parking.entity.ParkingLog;
import cn.edu.lnu.parking.entity.ParkingLogVo;
import cn.edu.lnu.parking.mapper.ParkingLogMapper;
import cn.edu.lnu.parking.service.CarNumService;
import cn.edu.lnu.parking.service.ParkingLogService;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingLogServiceImpl extends ServiceImpl<ParkingLogMapper, ParkingLog> implements ParkingLogService {

    @Autowired
    private CarNumService carNumService;

    @Override
    public Result getPage(ParkingLogVo vo) {
        LambdaQueryWrapper<ParkingLog> wrapper = new LambdaQueryWrapper<>();
        this.buildCondition(wrapper,vo);
        IPage<ParkingLog> page = this.page(new Page<>(vo.getPageNo(), vo.getPageSize()),wrapper);
        return Result.success(page);
    }

    @Override
    public Result getFrontPage(ParkingLogVo vo, Integer userId) {
        LambdaQueryWrapper<CarNum> carNumLambdaQueryWrapper = new LambdaQueryWrapper<>();
        carNumLambdaQueryWrapper.eq(CarNum::getUserId,userId);
        List<CarNum> list = carNumService.list(carNumLambdaQueryWrapper);
        if(ObjectUtils.isEmpty(list)){
            return Result.error("查无数据");
        }
        List<String> numList = list.stream().map(CarNum::getNum).collect(Collectors.toList());
        LambdaQueryWrapper<ParkingLog> wrapper = new LambdaQueryWrapper<>();
        this.buildCondition(wrapper,vo);
        wrapper.in(ParkingLog::getCarNum,numList);
        IPage<ParkingLog> page = this.page(new Page<>(vo.getPageNo(), vo.getPageSize()),wrapper);
        return Result.success(page);
    }

    private void buildCondition(LambdaQueryWrapper<ParkingLog> wrapper, ParkingLogVo vo) {
        if(ObjectUtils.isNotNull(vo.getAreaId())){
            wrapper.eq(ParkingLog::getAreaId,vo.getAreaId());
        }
        if(ObjectUtils.isNotNull(vo.getCode())){
            wrapper.like(ParkingLog::getCode,vo.getCode());
        }
        if(ObjectUtils.isNotNull(vo.getCarNum())){
            wrapper.like(ParkingLog::getCarNum,vo.getCarNum());
        }
        if(ObjectUtils.isNotNull(vo.getType())){
            wrapper.eq(ParkingLog::getType,vo.getType());
        }
        wrapper.orderByDesc(ParkingLog::getEndTime);
    }
}
