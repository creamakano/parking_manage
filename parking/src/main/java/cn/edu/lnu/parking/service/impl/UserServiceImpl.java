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
}
