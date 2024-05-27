package com.smartshoppingvalley.Order.dto;

import com.smartshoppingvalley.Commodity.entity.Commodity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommodityInOrders extends Commodity {
    //下单属性
    private String commodityAttribute;

    //下单数量
    private Integer number;

    //金额
    private BigDecimal amount;

    //商品图片
    private List<String> urls = new ArrayList<>();

    //是否评论 0否 1是
    private Integer isCommented;


    public static CommodityInOrders fromCommodity(Commodity commodity) {
        CommodityInOrders dto = new CommodityInOrders();
        BeanUtils.copyProperties(commodity, dto);
        return dto;
    }
}
