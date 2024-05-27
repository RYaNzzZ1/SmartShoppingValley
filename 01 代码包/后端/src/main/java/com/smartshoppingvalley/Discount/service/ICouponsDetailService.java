package com.smartshoppingvalley.Discount.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartshoppingvalley.Discount.entity.CouponsDetail;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 */
public interface ICouponsDetailService extends IService<CouponsDetail> {

    //用户查看领取的优惠券
    List<Long> getIdsByCustomerId(Long customerId);

    //按照优惠券id删除
    void removeByCouponsIds(Long ids);

    //优惠券已领取
    boolean isReceived(Long customerId, Long couponsId);

    //标记优惠券未已使用
    void use(Long customerId, List<Long> couponsIds);
}
