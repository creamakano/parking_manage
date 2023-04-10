package cn.edu.lnu.parking.service;

import cn.edu.lnu.parking.entity.User;
import cn.edu.lnu.parking.entity.UserVo;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;

public interface UserService  extends IService<User> {
    Result login(HttpSession session, User user);

    Result getPage(UserVo vo);

    Result registry(User user);
}
