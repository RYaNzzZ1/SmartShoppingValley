package com.smartshoppingvalley.Order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartshoppingvalley.Customer.entity.ShoppingCart;
import com.smartshoppingvalley.Order.entity.OrderDetail;
import com.smartshoppingvalley.Order.entity.Orders;
import com.smartshoppingvalley.Order.mapper.OrderDetailMapper;
import com.smartshoppingvalley.Order.service.IOrderDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

    //将订单明细返还购物车
    @Override
    public List<ShoppingCart> ordersTransferToShoppingCart(List<Orders> ordersList) {

        //要写入购物车的数据
        List<ShoppingCart> shoppingCartList = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        for (Orders orders : ordersList) {
            Long orderId = orders.getId();

            //根据订单id查明细
            List<OrderDetail> orderDetailList = this.listByOrderId(orderId);
            Long customerId = orders.getCustomerId();


            for (OrderDetail orderDetail : orderDetailList) {
                ShoppingCart shoppingCart = new ShoppingCart();

                shoppingCart.setCustomerId(customerId);
                BeanUtils.copyProperties(orderDetail, shoppingCart, "id", "orderId");
                shoppingCart.setCreateTime(now);

                shoppingCartList.add(shoppingCart);
            }
        }

        return shoppingCartList;
    }

    //根据订单id查明细
    @Override
    public List<OrderDetail> listByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getOrderId, orderId);
        return this.list(wrapper);
    }

    //清除订单明细
    @Override
    public void removeOrderDetails(List<Orders> ordersList) {
        //根据订单id删除明细
        List<Long> ids = new ArrayList<>();

        for (Orders orders : ordersList) {
            ids.add(orders.getId());
        }

        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(!ids.isEmpty(), OrderDetail::getOrderId, ids);
        this.remove(wrapper);
    }
}
