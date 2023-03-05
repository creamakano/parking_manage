package cn.edu.lnu.parking.service;

import cn.edu.lnu.parking.entity.CarNum;
import cn.edu.lnu.parking.entity.CarNumVo;
import cn.edu.lnu.parking.entity.User;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface CarNumService extends IService<CarNum> {
    Result getPage(CarNumVo vo);

    Result getList(CarNum carNum);

    List<String> getNumList(Integer userId);

    Result ListWithoutBuy(CarNum carNum);
}
