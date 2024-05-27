package com.smartshoppingvalley.Commodity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartshoppingvalley.Commodity.entity.CommodityAttribute;
import com.smartshoppingvalley.Commodity.mapper.CommodityAttributeMapper;
import com.smartshoppingvalley.Commodity.service.ICommodityAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜品口味关系表 服务实现类
 * </p>
 */
@Service
public class CommodityAttributeServiceImpl extends ServiceImpl<CommodityAttributeMapper, CommodityAttribute> implements ICommodityAttributeService {
    @Autowired
    private ICommodityAttributeService iCommodityAttributeService;

    public boolean deleteCommodityByCommodityId(Long commodityId) {
        QueryWrapper<CommodityAttribute> wrapper = new QueryWrapper<>();
        wrapper.eq("commodity_id", commodityId);
        return iCommodityAttributeService.remove(wrapper);
    }

    public List<CommodityAttribute> getAttribute(long commodityId) {
        QueryWrapper<CommodityAttribute> wrapper = new QueryWrapper<>();
        wrapper.eq("commodity_id", commodityId);
        return iCommodityAttributeService.list(wrapper);
    }

}
