package com.smartshoppingvalley.Customer.dto;

import com.smartshoppingvalley.Commodity.dto.CommodityDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 购物车
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ShoppingCartDto extends CommodityDto implements Serializable {

    private static final long serialVersionUID = 1L;

    //选择的属性
    private String commodityAttribute;

    //数量
    private Integer number;

    //金额
    private BigDecimal amount;


}
