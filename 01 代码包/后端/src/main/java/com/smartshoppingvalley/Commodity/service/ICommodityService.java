package com.smartshoppingvalley.Commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartshoppingvalley.Commodity.entity.Commodity;
import com.smartshoppingvalley.tools.Result;

/**
 * <p>
 * 菜品管理 服务类
 * </p>
 */
public interface ICommodityService extends IService<Commodity> {
    int sell_commodity(long id, Integer num);

    int delist_commodity(long id);

    Result screen_commodity(long categoryId, int p, int size);

    Result get_commodity(int page, int limit);


}
