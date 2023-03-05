package cn.edu.lnu.parking.mapper;

import cn.edu.lnu.parking.entity.Parking;
import cn.edu.lnu.parking.entity.RemainderVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ParkingMapper extends BaseMapper<Parking> {
    List<RemainderVo> getRemainder();

    void pickUp(@Param("id") Integer id);
}
