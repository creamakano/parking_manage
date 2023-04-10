package cn.edu.lnu.parking.service;

import cn.edu.lnu.parking.entity.Area;
import cn.edu.lnu.parking.entity.AreaVo;
import cn.edu.lnu.parking.entity.Card;
import cn.edu.lnu.parking.entity.CardVo;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CardService extends IService<Card> {
    //
    // Result getPage(AreaVo vo);
    //
    Result insert(CardVo vo);

    Result checkCarNum(CardVo vo);

    Result getPage(CardVo vo);

    boolean existCarNum(String carNum);
}
