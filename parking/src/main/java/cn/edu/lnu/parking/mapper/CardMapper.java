package cn.edu.lnu.parking.mapper;

import cn.edu.lnu.parking.entity.Area;
import cn.edu.lnu.parking.entity.Card;
import cn.edu.lnu.parking.entity.CardVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;


public interface CardMapper extends BaseMapper<Card> {
    IPage<CardVo> getPage(@Param("page") Page<CardVo> page, @Param("vo") CardVo vo);
}
