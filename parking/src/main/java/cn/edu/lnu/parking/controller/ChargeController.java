package cn.edu.lnu.parking.controller;

import cn.edu.lnu.parking.entity.Area;
import cn.edu.lnu.parking.entity.Charge;
import cn.edu.lnu.parking.entity.ChargeVo;
import cn.edu.lnu.parking.entity.User;
import cn.edu.lnu.parking.service.ChargeService;
import cn.edu.lnu.parking.util.Result;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/charge")
public class ChargeController {

    @Autowired
    private ChargeService chargeService;

    @PostMapping("/insert")
    public Result insert(@RequestBody Charge charge){
        return chargeService.insert(charge);
    }
    @GetMapping("/list")
    public Result list(){
        return Result.success(chargeService.list());
    }
    @GetMapping("/page")
    public Result list(ChargeVo vo){
        return chargeService.getPage(vo);
    }
    @PutMapping("/update")
    public Result update(@RequestBody Charge charge){
        return Result.success(chargeService.updateById(charge));
    }
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id){
        return Result.success(chargeService.removeById(id));
    }

    /**
     * 开始充电
     * @param charge
     * @return
     */
    @PostMapping("/start")
    public Result start(@RequestBody Charge charge){
        return chargeService.start(charge);
    }

    @PostMapping("/stop/{id}")
    public Result stop(@PathVariable("id") Integer id){
        return chargeService.stop(id);
    }

    @GetMapping("/front/page")
    public Result frontPage(ChargeVo vo, HttpSession session){
        User user = (User) session.getAttribute("LoginUser");
        if(user == null){
            return Result.unauthorized();
        }
        return chargeService.getFrontPage(vo,user.getId());
    }
}
