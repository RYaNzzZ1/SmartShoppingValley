package com.smartshoppingvalley.Discount.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CouponsDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 顾客id
     */
    private Long customerId;

    /**
     * 优惠券
     */
    private Long couponsId;

    /**
     * 是否用过 0否 1是
     */
    private Integer isUsed;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
