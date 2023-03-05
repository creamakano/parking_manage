package cn.edu.lnu.parking.entity;

import cn.edu.lnu.parking.util.BaseVo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
public class AreaVo extends BaseVo implements Serializable {
    private Integer id;
    private String name;
}
