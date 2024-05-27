package com.smartshoppingvalley.ImageFile.entity;

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
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 绑定类型的id
     */
    private Long associationId;

    /**
     * 类型 1商品 2评论 3售后
     */
    private Integer type;

    /**
     * 图片地址
     */
    private String url;

    /**
     * 创建时间（注册时间）
     */
    private LocalDateTime createTime;


}
