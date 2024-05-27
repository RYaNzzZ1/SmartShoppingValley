package com.smartshoppingvalley.Admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Time;

/**
 * <p>
 *
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 店铺名称
     */
    private String name;

    /**
     * logo
     */
    private String avatar;

    /**
     * 店铺介绍
     */
    private String description;

    /**
     * 开始营业时间
     */
    private Time openingTime;

    /**
     * 结束营业时间
     */
    private Time closingTime;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 联系信息
     */
    private String phone;


}
