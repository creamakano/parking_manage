package cn.edu.lnu.parking.service;

import cn.edu.lnu.parking.entity.Employee;
import cn.edu.lnu.parking.entity.EmployeeVo;
import cn.edu.lnu.parking.entity.User;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;

public interface EmployeeService extends IService<Employee> {
    Result login(HttpSession session, Employee employee);

    Result getPage(EmployeeVo vo);

}
