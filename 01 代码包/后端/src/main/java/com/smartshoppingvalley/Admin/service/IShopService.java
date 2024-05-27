package com.smartshoppingvalley.Admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartshoppingvalley.Admin.entity.Shop;

/**
 * <p>
 * 服务类
 * </p>
 */
public interface IShopService extends IService<Shop> {
    Shop getShop();

    int changeShop(Shop shop);
}
