package cn.edu.lnu.parking.mapper;

import cn.edu.lnu.parking.entity.Area;
import cn.edu.lnu.parking.entity.Charge;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface ChargeMapper extends BaseMapper<Charge> {
    Integer stopCharge(@Param("id") Integer id);
}
