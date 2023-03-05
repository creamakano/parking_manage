package cn.edu.lnu.parking.entity;

import cn.edu.lnu.parking.util.BaseVo;
import lombok.Data;

@Data
public class CarNumVo extends BaseVo {
    private Integer id;

    private Integer userId;

    private String num;

    private Integer type;
}
