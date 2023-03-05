package cn.edu.lnu.parking.entity;

import cn.edu.lnu.parking.util.BaseVo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ChargeLogVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 所停车辆
     */
    private String carNum;

    /**
     * 车位编号
     */
    private String code;

    /**
     * 车位类型
     */
    private Integer type;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 花费
     */
    private BigDecimal cost;
}
