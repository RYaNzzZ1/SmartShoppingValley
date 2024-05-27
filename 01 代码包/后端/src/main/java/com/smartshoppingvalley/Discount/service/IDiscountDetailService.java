package com.smartshoppingvalley.Discount.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartshoppingvalley.Discount.entity.DiscountDetail;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 */
public interface IDiscountDetailService extends IService<DiscountDetail> {

    //获取参与折扣的所有商品id
    List<Long> allCommodityIdsWithDiscount();
}
