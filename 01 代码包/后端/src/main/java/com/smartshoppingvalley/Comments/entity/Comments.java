package com.smartshoppingvalley.Comments.entity;

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
public class Comments implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long customerId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 商品id
     */
    private Long commodityId;

    /**
     * 打分
     */
    private Integer score;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 管理员对评论的回复
     */
    private String reply;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
