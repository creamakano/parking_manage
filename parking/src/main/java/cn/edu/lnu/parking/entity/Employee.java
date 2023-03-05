package cn.edu.lnu.parking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 员工编号
     */
    private String code;

    /**
     * 密码
     */
    private String password;

    /**
     * 住址
     */
    private String address;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 学历
     */
    private String education;

    /**
     * 年龄
     */
    private Integer age;
    /**
     * 性别
     */
    private Integer sex;

    /**
     * 职位
     */
    private String position;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;

    @TableLogic(value = "0",delval = "1")
    private Integer is_deleted;}
