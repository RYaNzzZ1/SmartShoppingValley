package com.smartshoppingvalley.Admin.dto;

import com.smartshoppingvalley.Admin.entity.Statistics;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StatisticsDto extends Statistics implements Serializable {

    private static final long serialVersionUID = 1L;

    //一周的每日销量
    List<Integer> nums = new ArrayList<>();

    //一周的每日销售额
    List<BigDecimal> amounts = new ArrayList<>();

}
