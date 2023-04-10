package cn.edu.lnu.parking.service.impl;

import cn.edu.lnu.parking.entity.User;
import cn.edu.lnu.parking.entity.UserVo;
import cn.edu.lnu.parking.mapper.UserMapper;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public Result login(HttpSession session, User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone,user.getPhone())
                .eq(User::getPassword,user.getPassword());
        User loginUser  = this.getOne(wrapper);
        if(ObjectUtils.isNotNull(loginUser)){
            session.setAttribute("LoginUser",loginUser);
            return Result.success(loginUser);
        }
        else {
            return Result.error("账户密码不匹配");
        }

    }

    @Override
    public Result getPage(UserVo vo) {
        Page<User> page = new Page<>(vo.getPageNo(), vo.getPageSize());
        IPage<User> iPage = this.page(page);
        return Result.success(iPage);
    }

    @Override
    public Result registry(User user) {
        if(ObjectUtils.isNull(user.getPhone(),user.getPassword(),user.getConfirmPassword())){
            return Result.error("请正确填写");
        }
        if(!user.getPassword().equals(user.getConfirmPassword())){
            return Result.error("两次密码输入不相同");
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone,user.getPhone());
        if(ObjectUtils.isNotEmpty(this.getOne(wrapper))){
            return Result.error("该手机号已被注册");
        }
        user.setPoint(0);
        user.setLevel(1);
        user.setName(user.getPhone());
        this.save(user);
        return Result.success();
    }
}
