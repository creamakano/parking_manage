package cn.edu.lnu.parking.entity;

import lombok.Data;

/**
 * 余位
 */
@Data
public class RemainderVo {
    private String area;

    private Integer car;

    private Integer motorcycle;
}
