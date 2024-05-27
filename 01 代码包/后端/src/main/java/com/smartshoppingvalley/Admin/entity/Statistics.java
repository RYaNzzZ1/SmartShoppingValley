package com.smartshoppingvalley.Admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 *
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Statistics implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 日销量
     */
    private Integer dailyNum;

    /**
     * 日销售额
     */
    private BigDecimal dailyAmount;

    /**
     * 周销量
     */
    private Integer weeklyNum;

    /**
     * 周销售额
     */
    private BigDecimal weeklyAmount;

    /**
     * 月销量
     */
    private Integer monthlyNum;

    /**
     * 月销售额
     */
    private BigDecimal monthlyAmount;

    /**
     * 总销量
     */
    private Integer totalNum;

    /**
     * 总销售额
     */
    private BigDecimal totalAmount;

    /**
     * 记录时间
     */
    private LocalDate createTime;


}
