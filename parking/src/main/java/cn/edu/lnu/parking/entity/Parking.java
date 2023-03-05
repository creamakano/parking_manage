package cn.edu.lnu.parking.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@TableName("t_parking")
@Data
public class Parking implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * code
     */
    private String code;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 1 机动车 2 二轮车
     */
    private Integer type;

    /**
     * car_num
     */
    private String carNum;

    /**
     * 0 临停 1 私人
     */
    private Integer isPrivate;

    /**
     * 区域id
     */
    private Integer areaId;

    /**
     * 停车开始时间
     */
    private Date startTime;

    private Integer userId;

    /**
     * 车牌号码
     */
    @TableField(exist = false)
    private String num;

    @TableField(exist = false)
    private String returnUrl;
}