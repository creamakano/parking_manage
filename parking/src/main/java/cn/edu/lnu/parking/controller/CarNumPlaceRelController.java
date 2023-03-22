package cn.edu.lnu.parking.controller;

import cn.edu.lnu.parking.entity.CarNumPlaceRel;
import cn.edu.lnu.parking.service.CarNumPlaceRelService;
import cn.edu.lnu.parking.service.PaymentService;
import cn.edu.lnu.parking.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("carNumPlaceRel")
public class CarNumPlaceRelController {
    @Autowired
    @Lazy
    private CarNumPlaceRelService relService;
    @PostMapping("/rent")
    public Result rent(@RequestBody CarNumPlaceRel rel)  {
       return relService.rent(rel);
    }

    /**
     * 收回车位
     */
    @PostMapping("/getBack")
    public Result getBack(@RequestBody CarNumPlaceRel rel)  {
       return relService.getBack(rel.getId());
    }
}
