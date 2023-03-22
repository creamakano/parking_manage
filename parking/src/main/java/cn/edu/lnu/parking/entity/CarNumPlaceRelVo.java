package cn.edu.lnu.parking.entity;

import cn.edu.lnu.parking.util.BaseVo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CarNumPlaceRelVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 停车车位id
     */
    private Integer parkingId;

    /**
     * 当前车位所属车牌号
     */
    private String carNum;

    /**
     * 车位所属车牌号
     */
    private String originalCarNum;

    /**
     * 租借 ：0未租借 1租借中
     */
    private int isRent;

    /**
     * 更改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    /**
     * 车位编号
     */
    private String code;
    /**
     * 车位类型
     */
    private Integer type;
    /**
     * 区域id
     */
    private Integer areaId;
}
