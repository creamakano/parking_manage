package cn.edu.lnu.parking.service.impl;

import cn.edu.lnu.parking.entity.Area;
import cn.edu.lnu.parking.entity.AreaVo;
import cn.edu.lnu.parking.entity.Card;
import cn.edu.lnu.parking.entity.CardVo;
import cn.edu.lnu.parking.mapper.AreaMapper;
import cn.edu.lnu.parking.mapper.CardMapper;
import cn.edu.lnu.parking.service.AreaService;
import cn.edu.lnu.parking.service.CarNumService;
import cn.edu.lnu.parking.service.CardService;
import cn.edu.lnu.parking.util.BaseVo;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements CardService {

    @Autowired
    private CarNumService carNumService;

    @Override
    public Result insert(CardVo vo) {
        LambdaQueryWrapper<Card> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Card::getCarNum,vo.getCarNum());
        long days = 30L;
        if(vo.getType() == 1){
            days = 365L;
        }
        Card card = this.getOne(wrapper);
        Date now = new Date();
        if(ObjectUtils.isNotNull(card)){
            Date endTime = card.getEndTime();
            if(endTime.getTime()< now.getTime()){
                endTime = now;
            }
            Long endTimeValue = endTime.getTime()+days*86400000L;
            Date endDate = new Date(endTimeValue);
            card.setEndTime(endDate);
            this.updateById(card);
        }else {
            card = new Card();
            card.setCarNum(vo.getCarNum());
            card.setEndTime(new Date(now.getTime() +days*86400000L));
            this.save(card);
        }

        return Result.success();
    }

    @Override
    public Result checkCarNum(CardVo vo) {
        boolean existCarNum = carNumService.existCarNum(vo.getCarNum(), vo.getUserId());
        return Result.success(existCarNum);
    }

    @Override
    public Result getPage(CardVo vo) {
        Page<CardVo> page = new Page<>(vo.getPageNo(),vo.getPageSize());
        IPage<CardVo> iPage =  baseMapper.getPage(page,vo);
        return Result.success(iPage);
    }

    @Override
    public boolean existCarNum(String carNum) {
        LambdaQueryWrapper<Card> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Card::getCarNum,carNum);
        wrapper.ge(Card::getEndTime,new Date());
        return ObjectUtils.isNotEmpty(this.getOne(wrapper));
    }
}
