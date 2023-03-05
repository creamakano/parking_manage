package cn.edu.lnu.parking.entity;

import cn.edu.lnu.parking.util.BaseVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ChargeVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
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
    private String startTime;

    /**
     * 充电持续时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date chargeTime;
}
