package cn.edu.lnu.parking.service;

import cn.edu.lnu.parking.entity.ParkingLog;
import cn.edu.lnu.parking.entity.ParkingLogVo;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ParkingLogService extends IService<ParkingLog> {

    Result getPage(ParkingLogVo vo);

}
