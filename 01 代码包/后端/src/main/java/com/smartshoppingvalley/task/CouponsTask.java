package com.smartshoppingvalley.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartshoppingvalley.Discount.entity.CouponsDetail;
import com.smartshoppingvalley.Discount.service.ICouponsDetailService;
import com.smartshoppingvalley.Discount.service.ICouponsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class CouponsTask {

    @Autowired
    private ICouponsService couponsService;

    @Autowired
    private ICouponsDetailService couponsDetailService;

    @Scheduled(cron = "0 0 4 * * *") // 每天四点执行
    @Transactional
    public void remove() {
        //获取失效优惠券ids
        List<Long> couponsIds = couponsService.getExpiredCoupons();

        if (couponsIds.isEmpty()) return;

        //删除用户与失效优惠券的关联
        LambdaQueryWrapper<CouponsDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CouponsDetail::getCouponsId, couponsIds);
        couponsDetailService.remove(wrapper);
    }
}
