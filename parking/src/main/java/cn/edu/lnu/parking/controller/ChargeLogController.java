package cn.edu.lnu.parking.controller;

import cn.edu.lnu.parking.entity.ChargeLog;
import cn.edu.lnu.parking.entity.ChargeLogVo;
import cn.edu.lnu.parking.service.ChargeLogService;
import cn.edu.lnu.parking.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chargeLog")
public class ChargeLogController {

    @Autowired
    private ChargeLogService chargeLogService;

    @PostMapping("/insert")
    public Result insert(@RequestBody ChargeLog chargeLog){
        return Result.success(chargeLogService.save(chargeLog));
    }
    @GetMapping("/list")
    public Result list(){
        return Result.success(chargeLogService.list());
    }
    @GetMapping("/page")
    public Result page(ChargeLogVo vo){
        return chargeLogService.getPage(vo);
    }
    @PutMapping("/update")
    public Result update(@RequestBody ChargeLog chargeLog){
        return Result.success(chargeLogService.updateById(chargeLog));
    }
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id){
        return Result.success(chargeLogService.removeById(id));
    }
}
