package cn.edu.lnu.parking.service;

import cn.edu.lnu.parking.entity.Parking;
import cn.edu.lnu.parking.entity.ParkingVo;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ParkingService extends IService<Parking> {
    Result insert(Parking parking);

    Result delete(Integer id);

    Result update(Parking parking);

    Result pageList(ParkingVo vo);

    Result getList(Parking parking);

    Result getRemainder();

    Result park(Parking parking);

    Result pickUp(Integer id, Parking parking);

    Result myPage(ParkingVo vo, Integer id);

    Result buy(Integer id, String num, Integer userId);

    Result myPlace(ParkingVo vo, Integer id);
}
