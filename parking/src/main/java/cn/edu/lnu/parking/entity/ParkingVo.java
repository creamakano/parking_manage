package cn.edu.lnu.parking.entity;

import cn.edu.lnu.parking.util.BaseVo;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
@Data
public class ParkingVo extends BaseVo implements Serializable {

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

    private Integer onlyPrivate;
}