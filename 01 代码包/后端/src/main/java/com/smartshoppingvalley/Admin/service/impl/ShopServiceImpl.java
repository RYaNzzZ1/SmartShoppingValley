package com.smartshoppingvalley.Admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartshoppingvalley.Admin.entity.Shop;
import com.smartshoppingvalley.Admin.mapper.ShopMapper;
import com.smartshoppingvalley.Admin.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Override
    public Shop getShop() {
        return shopMapper.selectOne(null);
    }

    @Override
    public int changeShop(Shop shop) {
        return shopMapper.update(shop, null);
    }
}
