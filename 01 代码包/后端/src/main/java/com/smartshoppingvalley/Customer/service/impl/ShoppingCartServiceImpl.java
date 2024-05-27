package com.smartshoppingvalley.Customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartshoppingvalley.Customer.entity.ShoppingCart;
import com.smartshoppingvalley.Customer.mapper.ShoppingCartMapper;
import com.smartshoppingvalley.Customer.service.IShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 购物车 服务实现类
 * </p>
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements IShoppingCartService {

    //清除用户购物车
    @Override
    public void clearByCustomerId(Long customerId) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getCustomerId, customerId);
        this.remove(wrapper);
    }

    //获取用户购物车列表
    @Override
    public List<ShoppingCart> listByCustomerId(Long customerId) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getCustomerId, customerId);
        return this.list(wrapper);
    }
}
