package cn.edu.lnu.parking.service;

import cn.edu.lnu.parking.entity.Area;
import cn.edu.lnu.parking.entity.AreaVo;
import cn.edu.lnu.parking.entity.Charge;
import cn.edu.lnu.parking.entity.ChargeVo;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ChargeService extends IService<Charge> {

    Result getPage(ChargeVo vo);

    Result insert(Charge charge);

    Result start(Charge charge);

    Result stop(Integer id);

    Result getFrontPage(ChargeVo vo, Integer userId);

    Result stop(Integer id, Integer userId);
}
