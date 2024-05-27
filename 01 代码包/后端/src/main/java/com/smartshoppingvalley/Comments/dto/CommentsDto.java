package com.smartshoppingvalley.Comments.dto;

import com.smartshoppingvalley.Comments.entity.Comments;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
public class CommentsDto extends Comments implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 图片urls
     */
    private List<String> urls = new ArrayList<>();

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像
     */
    private String url;
}
