package cn.edu.lnu.parking.controller;

import cn.edu.lnu.parking.entity.ParkingLog;
import cn.edu.lnu.parking.entity.ParkingLogVo;
import cn.edu.lnu.parking.service.ParkingLogService;
import cn.edu.lnu.parking.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parkingLog")
public class ParkingLogController {

    @Autowired
    private ParkingLogService parkingLogService;

    @PostMapping("/insert")
    public Result insert(@RequestBody ParkingLog parkingLog){
        return Result.success(parkingLogService.save(parkingLog));
    }
    @GetMapping("/list")
    public Result list(){
        return Result.success(parkingLogService.list());
    }
    @GetMapping("/page")
    public Result page(ParkingLogVo vo){
        return parkingLogService.getPage(vo);
    }
    @PutMapping("/update")
    public Result update(@RequestBody ParkingLog parkingLog){
        return Result.success(parkingLogService.updateById(parkingLog));
    }
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id){
        return Result.success(parkingLogService.removeById(id));
    }
}
