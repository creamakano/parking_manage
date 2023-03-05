package cn.edu.lnu.parking.controller;

import cn.edu.lnu.parking.entity.Employee;
import cn.edu.lnu.parking.entity.Employee;
import cn.edu.lnu.parking.entity.EmployeeVo;
import cn.edu.lnu.parking.entity.UserVo;
import cn.edu.lnu.parking.service.EmployeeService;
import cn.edu.lnu.parking.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public Result login(HttpSession session, @RequestBody Employee employee){
        return employeeService.login(session,employee);
    }
    @PostMapping("/logout")
    public Result logout(HttpSession session){
        session.removeAttribute("LoginEmployee");
        return Result.success();
    }

    @RequestMapping("getSession")
    public Result get(HttpSession session){
        Employee employee = (Employee) session.getAttribute("LoginEmployee");
        System.out.println("LoginEmployee = " + employee);
        return Result.success(employee);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Employee employee){
        return Result.success(employeeService.updateById(employee));
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id){
        return Result.success(employeeService.removeById(id));
    }

    @GetMapping("/page")
    public Result page(EmployeeVo vo){
        return employeeService.getPage(vo);
    }


}
