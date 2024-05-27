package com.smartshoppingvalley.Order.dto;

import com.smartshoppingvalley.Order.entity.OrderDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单明细表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderDetailDto extends OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 地址簿id
     */
    private Long addressBookId;

    /**
     * 支付方式
     */
    private Integer payMethod;

    /**
     * 备注
     */
    private String remark;

    /**
     * 使用的优惠券
     */
    private List<Long> couponsIds = new ArrayList<>();
}
