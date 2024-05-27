package com.smartshoppingvalley.Order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartshoppingvalley.Order.entity.Orders;
import com.smartshoppingvalley.Order.mapper.OrderMapper;
import com.smartshoppingvalley.Order.service.IOrderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements IOrderService {

    //查询未付款订单
    @Override
    public List<Orders> getUnpaidOrders() {
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getStatus, 1);
        return this.list(wrapper);
    }

    //取消超时订单
    @Override
    public void removeOrders(List<Orders> ordersList) {
        //要删除的订单id
        List<Long> ids = new ArrayList<>();

        for (Orders orders : ordersList) {
            ids.add(orders.getId());
        }

        this.removeByIds(ids);
    }

    //根据用户id查询订单列表
    @Override
    public List<Orders> listByCustomerId(Long customerId) {
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getCustomerId, customerId);
        wrapper.eq(Orders::getIsDeleted, 0);
        return this.list(wrapper);
    }

}
