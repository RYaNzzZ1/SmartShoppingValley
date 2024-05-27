package com.smartshoppingvalley.Discount.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartshoppingvalley.Discount.entity.Coupons;
import com.smartshoppingvalley.Discount.mapper.CouponsMapper;
import com.smartshoppingvalley.Discount.service.ICouponsService;
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
public class CouponsServiceImpl extends ServiceImpl<CouponsMapper, Coupons> implements ICouponsService {

    //获取失效优惠券ids
    @Override
    public List<Long> getExpiredCoupons() {
        LocalDate now = LocalDate.now();

        LambdaQueryWrapper<Coupons> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Coupons::getStatus, 1);
        wrapper.le(Coupons::getEndTime, now);
        List<Coupons> couponsList = this.list(wrapper);

        List<Long> ids = new ArrayList<>();
        for (Coupons coupons : couponsList) {
            ids.add(coupons.getId());
        }

        return ids;
    }
}
