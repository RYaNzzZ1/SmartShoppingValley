package com.smartshoppingvalley.Discount.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartshoppingvalley.Discount.entity.Discount;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 */
public interface IDiscountService extends IService<Discount> {

    //获取失效折扣ids
    List<Long> getExpiredDiscounts();
}
