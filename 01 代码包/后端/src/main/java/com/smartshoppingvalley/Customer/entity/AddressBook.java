package com.smartshoppingvalley.Customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 地址管理
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AddressBook implements Serializable {

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
     * 收货人
     */
    private String consignee;

    /**
     * 性别 1 男 2女
     */
    private Integer sex;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 默认 0 否 1是
     */
    private Integer isDefault;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否删除0否 1是
     */
    private Integer isDeleted;

    public AddressBook() {
        this.createTime = LocalDateTime.now();
    }
}
