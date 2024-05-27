package com.smartshoppingvalley.Order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartshoppingvalley.Order.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}
