package cn.edu.lnu.parking.service;

import cn.edu.lnu.parking.entity.ChargeLog;
import cn.edu.lnu.parking.entity.ChargeLogVo;
import cn.edu.lnu.parking.entity.ParkingLog;
import cn.edu.lnu.parking.entity.ParkingLogVo;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ChargeLogService extends IService<ChargeLog> {
    Result getPage(ChargeLogVo vo);

    // Result getPage(ParkingLogVo vo);

}
