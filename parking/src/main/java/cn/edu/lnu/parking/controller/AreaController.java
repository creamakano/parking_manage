package cn.edu.lnu.parking.controller;

import cn.edu.lnu.parking.entity.Area;
import cn.edu.lnu.parking.entity.AreaVo;
import cn.edu.lnu.parking.service.AreaService;
import cn.edu.lnu.parking.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/area")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @PostMapping("/insert")
    public Result insert(@RequestBody Area area){

        return areaService.insert(area);
    }
    @GetMapping("/list")
    public Result list(){
        return Result.success(areaService.list());
    }
    @GetMapping("/page")
    public Result list(AreaVo vo){
        return areaService.getPage(vo);
    }
    @PutMapping("/update")
    public Result update(@RequestBody Area area){
        return Result.success(areaService.updateById(area));
    }
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id){
        return Result.success(areaService.removeById(id));
    }
}
