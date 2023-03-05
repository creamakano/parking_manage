package cn.edu.lnu.parking.service.impl;

import cn.edu.lnu.parking.entity.Area;
import cn.edu.lnu.parking.entity.AreaVo;
import cn.edu.lnu.parking.entity.Parking;
import cn.edu.lnu.parking.entity.ParkingVo;
import cn.edu.lnu.parking.mapper.AreaMapper;
import cn.edu.lnu.parking.mapper.ParkingMapper;
import cn.edu.lnu.parking.service.AreaService;
import cn.edu.lnu.parking.service.ParkingService;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {

    @Override
    public Result getPage(AreaVo vo) {
        if(vo.getPageNo()==null){
            vo.setPageNo(1);
        }
        if(vo.getPageSize()==null){
            vo.setPageSize(10);
        }
        LambdaQueryWrapper<Area> wrapper = new LambdaQueryWrapper<>();
        buildCondition(vo.getName(), wrapper);
        IPage<Area> page = this.page(new Page<>(vo.getPageNo(), vo.getPageSize()),wrapper);
        return Result.success(page);
    }

    private void buildCondition(String name, LambdaQueryWrapper<Area> wrapper) {
        if(ObjectUtils.isNotNull(name)){
            wrapper.like(Area::getName,name);
        }
    }

    @Override
    public Result insert(Area area) {
        LambdaQueryWrapper<Area> wrapper = new LambdaQueryWrapper<>();
        buildCondition(area.getName(), wrapper);
        if(ObjectUtils.isNotNull(this.list(wrapper))){
            return Result.error("名称重复，请重新输入");
        }
        this.save(area);
        return Result.success();
    }
}
