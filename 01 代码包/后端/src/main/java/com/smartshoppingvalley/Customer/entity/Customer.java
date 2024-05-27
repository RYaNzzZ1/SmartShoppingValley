package com.smartshoppingvalley.Customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author wyh
 * @since 2023-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO) // 设置自增主键
    private Long id;

    /**
     * 用户名（邮箱）
     */
    @Getter
    private String username;

    /**
     * 密码
     */
    @Getter
    private String password;

    /**
     * 状态：1:正常，2:禁用
     */
    @Getter
    private Integer status;

    /**
     * 创建时间（注册时间）
     */
    @Getter
    private LocalDateTime createTime;


    public Customer() {
    }

    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
        this.createTime = LocalDateTime.now();
        this.status = 1;
    }

}
