package cn.edu.lnu.parking.entity;

import cn.edu.lnu.parking.util.BaseVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CardVo extends BaseVo implements Serializable {
    private Integer id;
    private String carNum;
    private Date endTime;
    private Integer days;
    private Integer userId;
    /**
     * 1年卡 2月卡
     */
    private Integer type;
    private Integer carType;
    private String returnUrl;
}
