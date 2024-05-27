package com.smartshoppingvalley.Order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartshoppingvalley.Order.entity.Orders;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 */
public interface IOrderService extends IService<Orders> {

    //查询未付款订单
    List<Orders> getUnpaidOrders();

    //取消超时订单
    void removeOrders(List<Orders> timeoutOrders);

    //根据用户id查询订单列表
    List<Orders> listByCustomerId(Long customerId);

}
