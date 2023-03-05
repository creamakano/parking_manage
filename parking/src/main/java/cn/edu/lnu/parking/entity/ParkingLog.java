package cn.edu.lnu.parking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_parking_log")
public class ParkingLog {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String carNum;

    private Integer areaId;

    private String code;

    private Integer type;

    private Date startTime;

    private Date endTime;

    private Double cost;

}
