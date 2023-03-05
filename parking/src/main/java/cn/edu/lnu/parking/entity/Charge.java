package cn.edu.lnu.parking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
@Data
@TableName("t_charge")
public class Charge implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 充电位编号
     */
    private String code;

    /**
     * 充电状态 0：未充电 1：充电中
     */
    private Integer status;

    /**
     * 1 机动车 2 二轮车
     */
    private Integer type;

    /**
     * 充电车辆
     */
    private String carNum;

    /**
     * 充电开始时间
     */
    private Date startTime;

    /**
     * 充电持续时间
     */
    private String chargeTime;
    /**
     * 车牌
     */
    @TableField(exist = false)
    private String num;
}
