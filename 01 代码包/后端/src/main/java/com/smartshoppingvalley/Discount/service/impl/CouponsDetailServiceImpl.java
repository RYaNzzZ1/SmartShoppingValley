package com.smartshoppingvalley.Discount.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartshoppingvalley.Discount.entity.CouponsDetail;
import com.smartshoppingvalley.Discount.mapper.CouponsDetailMapper;
import com.smartshoppingvalley.Discount.service.ICouponsDetailService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Service
public class CouponsDetailServiceImpl extends ServiceImpl<CouponsDetailMapper, CouponsDetail> implements ICouponsDetailService {

    //用户查看领取的优惠券
    @Override
    public List<Long> getIdsByCustomerId(Long customerId) {
        LambdaQueryWrapper<CouponsDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(customerId != null, CouponsDetail::getCustomerId, customerId);
        wrapper.eq(CouponsDetail::getIsUsed, 1);
        List<CouponsDetail> detailList = this.list(wrapper);

        //已领取优惠券id
        List<Long> ids = new ArrayList<>();
        for (CouponsDetail detail : detailList) {
            ids.add(detail.getCouponsId());
        }

        return ids;
    }

    //按照优惠券id删除
    @Override
    public void removeByCouponsIds(Long ids) {
        LambdaQueryWrapper<CouponsDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CouponsDetail::getCouponsId, ids);
        this.remove(wrapper);
    }

    //优惠券已领取
    @Override
    public boolean isReceived(Long customerId, Long couponsId) {
        //优惠券已领取
        LambdaQueryWrapper<CouponsDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CouponsDetail::getCouponsId, couponsId);
        wrapper.eq(CouponsDetail::getCustomerId, customerId);
        List<CouponsDetail> list = this.list(wrapper);
        return !list.isEmpty();
    }

    //标记优惠券未已使用
    @Override
    public void use(Long customerId, List<Long> couponsIds) {
        if (couponsIds.isEmpty()) return;

        LambdaUpdateWrapper<CouponsDetail> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CouponsDetail::getCustomerId, customerId);
        wrapper.in(CouponsDetail::getCouponsId, couponsIds);
        wrapper.set(CouponsDetail::getIsUsed, 1);
        this.update(wrapper);
    }
}
