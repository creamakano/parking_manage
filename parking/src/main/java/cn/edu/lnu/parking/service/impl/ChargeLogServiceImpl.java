package cn.edu.lnu.parking.service.impl;

import cn.edu.lnu.parking.entity.ChargeLog;
import cn.edu.lnu.parking.entity.ChargeLogVo;
import cn.edu.lnu.parking.mapper.ChargeLogMapper;
import cn.edu.lnu.parking.service.ChargeLogService;
import cn.edu.lnu.parking.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ChargeLogServiceImpl extends ServiceImpl<ChargeLogMapper, ChargeLog> implements ChargeLogService {
    @Override
    public Result getPage(ChargeLogVo vo) {
        LambdaQueryWrapper<ChargeLog> wrapper = new LambdaQueryWrapper<>();
        this.buildCondition(wrapper, vo);
        IPage<ChargeLog> page = this.page(new Page<>(vo.getPageNo(), vo.getPageSize()), wrapper);
        return Result.success(page);
    }

    private void buildCondition(LambdaQueryWrapper<ChargeLog> wrapper, ChargeLogVo vo) {
        if (ObjectUtils.isNotNull(vo.getCarNum())) {
            wrapper.like(ChargeLog::getCarNum, vo.getCarNum());
        }
        if (ObjectUtils.isNotNull(vo.getCode())) {
            wrapper.like(ChargeLog::getCode, vo.getCode());
        }
        if (vo.getType() != null) {
            wrapper.eq(ChargeLog::getType, vo.getType());
        }
    }
}
