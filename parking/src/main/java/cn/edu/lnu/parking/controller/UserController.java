package cn.edu.lnu.parking.controller;

import cn.edu.lnu.parking.entity.User;
import cn.edu.lnu.parking.entity.UserVo;
import cn.edu.lnu.parking.service.UserService;
import cn.edu.lnu.parking.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(HttpSession session, @RequestBody User user){
        return userService.login(session,user);
    }

    @PostMapping("/registry")
    public Result registry(@RequestBody User user){
        return userService.registry(user);
    }

    @PostMapping("/logout")
    public Result logout(HttpSession session){
        session.removeAttribute("LoginUser");
        return Result.success();
    }

    @RequestMapping("getSession")
    public Result get(HttpSession session){
        User user = (User) session.getAttribute("LoginUser");
        return Result.success(user);
    }
    @RequestMapping("getInfo")
    public Result getInfo(HttpSession session){
        User user = (User) session.getAttribute("LoginUser");
        if(user == null){
            return Result.unauthorized();
        }
        return Result.success(userService.getById(user.getId()));
    }

    @PutMapping("/update")
    public Result update(@RequestBody User user){
        return Result.success(userService.updateById(user));
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id){
        return Result.success(userService.removeById(id));
    }

    @GetMapping("page")
    public Result page(UserVo vo){
        return userService.getPage(vo);
    }
}
