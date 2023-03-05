package cn.edu.lnu.parking.service;

import cn.edu.lnu.parking.entity.Area;
import cn.edu.lnu.parking.entity.AreaVo;
import cn.edu.lnu.parking.entity.Parking;
import cn.edu.lnu.parking.entity.ParkingVo;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AreaService extends IService<Area> {

    Result getPage(AreaVo vo);

    Result insert(Area area);
}
