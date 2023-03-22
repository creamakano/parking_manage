package cn.edu.lnu.parking.controller;

import cn.edu.lnu.parking.entity.*;
import cn.edu.lnu.parking.service.AreaService;
import cn.edu.lnu.parking.service.CardService;
import cn.edu.lnu.parking.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 年卡月卡控制层
 */
@RestController
@RequestMapping("/card")
public class CardController{

    @Autowired
    private CardService cardService;

    @PostMapping("/insert")
    public Result insert(@RequestBody CardVo vo, HttpSession session){
        User user = (User) session.getAttribute("LoginUser");
        if(user == null){
            return Result.unauthorized();
        }
        vo.setUserId(user.getId());
        return cardService.insert(vo);
    }
    @PostMapping("/checkCarNum")
    public Result checkCarNum(@RequestBody CardVo vo, HttpSession session){
        User user = (User) session.getAttribute("LoginUser");
        if(user == null){
            return Result.unauthorized();
        }
        vo.setUserId(user.getId());
        return cardService.checkCarNum(vo);
    }
    @GetMapping("/page")
    public Result page(CardVo vo, HttpSession session){
        User user = (User) session.getAttribute("LoginUser");
        if(user == null){
            return Result.unauthorized();
        }
        vo.setUserId(user.getId());
        return cardService.getPage(vo);
    }



    // @GetMapping("/list")
    // public Result list(){
    //     return Result.success(areaService.list());
    // }
    // @GetMapping("/page")
    // public Result list(AreaVo vo){
    //     return areaService.getPage(vo);
    // }
    // @PutMapping("/update")
    // public Result update(@RequestBody Area area){
    //     return Result.success(areaService.updateById(area));
    // }
    // @PostMapping("/delete/{id}")
    // public Result delete(@PathVariable("id") Integer id){
    //     return Result.success(areaService.removeById(id));
    // }
}
