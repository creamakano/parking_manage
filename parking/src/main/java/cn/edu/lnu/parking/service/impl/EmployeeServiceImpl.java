package cn.edu.lnu.parking.service.impl;

import cn.edu.lnu.parking.entity.Employee;
import cn.edu.lnu.parking.entity.EmployeeVo;
import cn.edu.lnu.parking.entity.User;
import cn.edu.lnu.parking.mapper.EmployeeMapper;
import cn.edu.lnu.parking.mapper.UserMapper;
import cn.edu.lnu.parking.service.EmployeeService;
import cn.edu.lnu.parking.service.UserService;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Override
    public Result login(HttpSession session, Employee employee) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getCode,employee.getCode())
                .eq(Employee::getPassword,employee.getPassword());
        Employee loginEmployee  = this.getOne(wrapper);
        if(ObjectUtils.isNotNull(loginEmployee)){
            session.setAttribute("LoginEmployee",loginEmployee);
            return Result.success(loginEmployee);
        }
        else {
            return Result.error("账户密码不匹配");
        }
    }

    @Override
    public Result getPage(EmployeeVo vo) {
        Page<Employee> page = new Page<>(vo.getPageNo(), vo.getPageSize());
        IPage<Employee> iPage = this.page(page);
        return Result.success(iPage);
    }

    @Override
    public Result insert(Employee employee) {
        if(ObjectUtils.isNull(employee.getCode(),employee.getPassword(),employee.getName(),employee.getPosition())){
            return Result.error("请正确填写信息");
        }
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getCode,employee.getCode());
        if(ObjectUtils.isNotEmpty(this.getOne(wrapper))){
            return Result.error("该员工编号已存在");
        }
        this.save(employee);
        return Result.success();
    }

}
