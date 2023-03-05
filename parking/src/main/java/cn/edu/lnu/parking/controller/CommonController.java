package cn.edu.lnu.parking.controller;

import cn.edu.lnu.parking.entity.Area;
import cn.edu.lnu.parking.service.AreaService;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/common")
public class CommonController {
    @Autowired
    private AreaService areaService;

    // @GetMapping("/dict/{type}")
    // public Result getDict(@PathVariable("type") String type){
    //     if (type.equals("area")){
    //         LambdaQueryWrapper<Area> wrapper = new LambdaQueryWrapper<>();
    //
    //         List<Area> list = areaService.list();
    //
    //     }
    //
    //     return Result.error("无此类型字典");
    // }
}
