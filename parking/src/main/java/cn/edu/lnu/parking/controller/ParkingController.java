package cn.edu.lnu.parking.controller;

import cn.edu.lnu.parking.entity.*;
import cn.edu.lnu.parking.service.ParkingService;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @description parking
 * @author zhengkai.blog.csdn.net
 * @date 2023-02-09
 */
@RestController
@RequestMapping(value = "/parking")
public class ParkingController {

    @Autowired
    @Lazy
    private ParkingService parkingService;

    /**
     * 新增
     **/
    @RequestMapping("/insert")
    public Result insert(@RequestBody Parking parking){
        return parkingService.insert(parking);
    }

    /**
     * 刪除
     **/
    @RequestMapping("/delete/{id}")
    public Result delete(@PathVariable("id")Integer id){
        return parkingService.delete(id);
    }

    /**
     * 更新
     **/
    @RequestMapping("/update")
    public Result update(@RequestBody Parking parking){
        return parkingService.update(parking);
    }

    /**
     * 查询 分页查询
     **/
    @RequestMapping("/page")
    public Result pageList(ParkingVo vo) {
        return parkingService.pageList(vo);
    }
    /**
     * 查询 列表
     **/
    @RequestMapping("/list")
    public Result list(Parking parking) {
        return parkingService.getList(parking);
    }


    /**
     * 余位
     */
    @GetMapping("getRemainder")
    public Result getRemainder(){
        return parkingService.getRemainder();
    }

    /**
     * 停车
     * @param parking
     * @return
     */
    @PostMapping("/park")
    public Result park(@RequestBody Parking parking){
        return parkingService.park(parking);
    }
    /**
     * 取车
     */
    @PostMapping("/pickUp/{id}")
    public Result pickUp(@PathVariable("id") Integer id,@RequestBody Parking parking,HttpSession session){
        return parkingService.pickUp(id,parking);
    }
    /**
     * 前台取车
     */
    @PostMapping("/front/pickUp")
    public Result frontPickUp(@RequestBody Parking parking,HttpSession session){
        User user = (User) session.getAttribute("LoginUser");
        if(user == null){
            return Result.unauthorized();
        }
        String returnUrl = parking.getReturnUrl();
        parking = parkingService.getById(parking.getId());
        parking.setReturnUrl(returnUrl);
        parking.setUserId(user.getId());
        return parkingService.frontPickUp(parking);
    }
    /**
     * 前台根据车牌号取车
     */
    @PostMapping("/front/pickUpByCarNum")
    public Result pickUpByCarNum(@RequestBody Parking parking,HttpSession session){
        User user = (User) session.getAttribute("LoginUser");
        if(user == null){
            return Result.unauthorized();
        }
        String returnUrl = parking.getReturnUrl();
        parking = parkingService.getByCarNum(parking.getNum());
        if(parking == null){
            return Result.error("查无该车");
        }
        parking.setReturnUrl(returnUrl);
        parking.setUserId(user.getId());
        return parkingService.frontPickUp(parking);
    }
    /**
     * 后台根据车牌号取车
     */
    @PostMapping("/back/pickUpByCarNum")
    public Result backPickUpByCarNum(@RequestBody Parking parking){
        String returnUrl = parking.getReturnUrl();
        parking = parkingService.getByCarNum(parking.getNum());
        if(parking == null){
            return Result.error("查无该车");
        }
        parking.setReturnUrl(returnUrl);
        return parkingService.backPickUp(parking);
    }


    /**
     * 查询 分页查询
     **/
    @RequestMapping("/front/page")
    public Result page(ParkingVo vo, HttpSession session) {
        User user = (User) session.getAttribute("LoginUser");
        if(user == null){
            return Result.unauthorized();
        }
        return parkingService.myPage(vo,user.getId());
    }

    /**
     * 购买
     **/
    @PostMapping("/buy")
    public Result buy(@RequestBody Parking parking,HttpSession session){
        if(ObjectUtils.isNull(parking.getId(),parking.getNum())){
            return Result.error("参数错误");
        }
        User user = (User) session.getAttribute("LoginUser");
        if(user == null){
            return Result.unauthorized();
        }
        return parkingService.buy(parking.getId(),parking.getNum(),user.getId());
    }

    @RequestMapping("/front/myPlace")
    public Result myPlace(CarNumPlaceRelVo vo, HttpSession session) {
        User user = (User) session.getAttribute("LoginUser");
        if(user == null){
            return Result.unauthorized();
        }
        vo.setUserId(user.getId());
        return parkingService.myPlace(vo);
    }

    /**
     * 前台首页，根据车牌号停车
     */
    @PostMapping("/front/parkByCarNum")
    public Result parkByCarNum(@RequestBody CarNum carNum, HttpSession session) {
        return parkingService.parkByCarNum(carNum);
    }
}