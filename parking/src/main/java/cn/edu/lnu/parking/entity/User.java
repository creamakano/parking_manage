package cn.edu.lnu.parking.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String phone;

    private String password;

    private Integer sex;

    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;

    private Integer point;

    private Integer level;
    @TableLogic(value = "0",delval = "1")
    private Integer is_deleted;

    @TableField(exist = false)
    private String confirmPassword;
}
