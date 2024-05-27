package com.smartshoppingvalley.Commodity.dto;

import com.smartshoppingvalley.Commodity.entity.Commodity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜品口味关系表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CommodityAttributeDto extends Commodity implements Serializable {

    private static final long serialVersionUID = 1L;

    //商品图片
    private List<String> urls = new ArrayList<>();

    //商品属性
    private List<Map<String, Object>> commodityAttributeList = new ArrayList<>();
}
