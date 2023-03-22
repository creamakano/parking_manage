package cn.edu.lnu.parking.mapper;

import cn.edu.lnu.parking.entity.CarNumPlaceRel;
import cn.edu.lnu.parking.entity.CarNumPlaceRelVo;
import cn.edu.lnu.parking.entity.Parking;
import cn.edu.lnu.parking.entity.RemainderVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ParkingMapper extends BaseMapper<Parking> {
    List<RemainderVo> getRemainder();

    void pickUp(@Param("id") Integer id);

    IPage<CarNumPlaceRelVo> getPage(@Param("page") Page<CarNumPlaceRel> page,@Param("vo") CarNumPlaceRelVo vo);
}
