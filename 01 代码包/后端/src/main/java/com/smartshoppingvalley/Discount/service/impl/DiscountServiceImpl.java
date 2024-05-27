package com.smartshoppingvalley.Discount.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartshoppingvalley.Discount.entity.Discount;
import com.smartshoppingvalley.Discount.mapper.DiscountMapper;
import com.smartshoppingvalley.Discount.service.IDiscountService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Service
public class DiscountServiceImpl extends ServiceImpl<DiscountMapper, Discount> implements IDiscountService {

    //获取失效折扣ids
    @Override
    public List<Long> getExpiredDiscounts() {
        LocalDate now = LocalDate.now();

        LambdaQueryWrapper<Discount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Discount::getStatus, 1);
        wrapper.le(Discount::getEndTime, now);
        List<Discount> discountList = this.list(wrapper);

        List<Long> ids = new ArrayList<>();
        for (Discount discount : discountList) {
            ids.add(discount.getId());
        }

        return ids;
    }
}
