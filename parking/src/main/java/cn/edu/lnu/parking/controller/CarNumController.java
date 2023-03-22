package cn.edu.lnu.parking.controller;

import cn.edu.lnu.parking.entity.CarNum;
import cn.edu.lnu.parking.entity.CarNumVo;
import cn.edu.lnu.parking.entity.User;
import cn.edu.lnu.parking.service.CarNumService;
import cn.edu.lnu.parking.util.Result;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/carNum")
public class CarNumController {

    @Autowired
    private CarNumService carNumService;

    @PostMapping("/insert")
    public Result insert(@RequestBody CarNum carNum){
        return Result.success(carNumService.save(carNum));
    }
    @GetMapping("/list")
    public Result list(){
        return Result.success(carNumService.list());
    }
    @GetMapping("/page")
    public Result page(CarNumVo vo){
        return carNumService.getPage(vo);
    }
    @PutMapping("/update")
    public Result update(@RequestBody CarNum carNum){
        return Result.success(carNumService.updateById(carNum));
    }
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id){
        return Result.success(carNumService.removeById(id));
    }
    @DeleteMapping("/front/delete/{id}")
    public Result frontDelete(@PathVariable("id") Integer id,HttpSession session){
        User user = (User) session.getAttribute("LoginUser");
        if(user == null){
            return Result.unauthorized();
        }
        return Result.success(carNumService.removeById(id));
    }

    @GetMapping("/front/page")
    public Result frontPage(CarNumVo vo, HttpSession session){
        User user = (User) session.getAttribute("LoginUser");
        if(user == null){
            return Result.unauthorized();
        }
        vo.setUserId(user.getId());
        return carNumService.getPage(vo);
    }

    @PostMapping("/front/insert")
    public Result frontInsert(@RequestBody CarNum carNum, HttpSession session){
        User user = (User) session.getAttribute("LoginUser");
        if(user == null){
            return Result.unauthorized();
        }
        carNum.setUserId(user.getId());
        return Result.success(carNumService.save(carNum));
    }
    @PutMapping("/front/update")
    public Result frontUpdate(@RequestBody CarNum carNum, HttpSession session){
        User user = (User) session.getAttribute("LoginUser");
        if(user == null){
            return Result.unauthorized();
        }
        return carNumService.updateCarNum(carNum);
    }
    @GetMapping("/front/list")
    public Result frontList(HttpSession session,CarNum carNum){
        User user = (User) session.getAttribute("LoginUser");
        if(user == null) {
            return Result.unauthorized();
        }
        carNum.setUserId(user.getId());
        return carNumService.getList(carNum);
    }
    @GetMapping("/front/listWithoutBuy")
    public Result frontListWithoutBuy(HttpSession session,CarNum carNum){
        User user = (User) session.getAttribute("LoginUser");
        if(user == null) {
            return Result.unauthorized();
        }
        carNum.setUserId(user.getId());
        return carNumService.ListWithoutBuy(carNum);
    }
}
