package com.smartshoppingvalley.Customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartshoppingvalley.Customer.entity.ShoppingCart;

import java.util.List;

/**
 * <p>
 * 购物车 服务类
 * </p>
 */
public interface IShoppingCartService extends IService<ShoppingCart> {

    //清除用户购物车
    void clearByCustomerId(Long customerId);

    //获取用户购物车列表
    List<ShoppingCart> listByCustomerId(Long customerId);
}
