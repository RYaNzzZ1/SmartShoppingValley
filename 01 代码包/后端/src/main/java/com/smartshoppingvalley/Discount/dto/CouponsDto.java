package com.smartshoppingvalley.Discount.dto;

import com.smartshoppingvalley.Discount.entity.Coupons;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CouponsDto extends Coupons implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 是否用过 0否 1是
     */
    private Integer isUsed;

    /**
     * 是否过期 0否 1是
     */
//    private int isExpired;

}
