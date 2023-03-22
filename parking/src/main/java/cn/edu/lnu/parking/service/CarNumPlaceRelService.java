package cn.edu.lnu.parking.service;

import cn.edu.lnu.parking.entity.CarNumPlaceRel;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CarNumPlaceRelService extends IService<CarNumPlaceRel> {

    boolean existCarNum(String carNum);

    Integer getPrivateParkingId(String carNum);

    Result rent(CarNumPlaceRel rel);

    Result getBack(Integer id);

    CarNumPlaceRel getByParkingId(Integer id);
}
