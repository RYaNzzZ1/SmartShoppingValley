package com.smartshoppingvalley.Comments.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartshoppingvalley.Comments.entity.Comments;

/**
 * <p>
 * 服务类
 * </p>
 */
public interface ICommentsService extends IService<Comments> {

    //用户对商品是否评论
    boolean isCommented(Long customerId, Long orderId, Long commodityId);
}
