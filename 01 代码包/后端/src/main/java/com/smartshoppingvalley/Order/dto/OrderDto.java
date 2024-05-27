package com.smartshoppingvalley.Order.dto;

import com.smartshoppingvalley.Customer.entity.AddressBook;
import com.smartshoppingvalley.Order.entity.Orders;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderDto extends Orders implements Serializable {

    private static final long serialVersionUID = 1L;
    //商品信息
    List<CommodityInOrders> commodityInOrdersList = new ArrayList<>();
    //地址簿
    private AddressBook addressBook = new AddressBook();
    /**
     * 使用的优惠券，提交订单时使用
     */
    private List<Long> couponsIds = new ArrayList<>();
}
