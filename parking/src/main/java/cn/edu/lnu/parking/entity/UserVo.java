package cn.edu.lnu.parking.entity;

import cn.edu.lnu.parking.util.BaseVo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class UserVo extends BaseVo {
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
}
