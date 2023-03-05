package cn.edu.lnu.parking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_car_num")
public class CarNum {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String num;

    private Integer type;
}
