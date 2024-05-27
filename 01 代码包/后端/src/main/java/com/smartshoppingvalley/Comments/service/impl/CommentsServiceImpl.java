package com.smartshoppingvalley.Comments.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartshoppingvalley.Comments.entity.Comments;
import com.smartshoppingvalley.Comments.mapper.CommentsMapper;
import com.smartshoppingvalley.Comments.service.ICommentsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements ICommentsService {

    //用户对商品是否评论
    @Override
    public boolean isCommented(Long customerId, Long orderId, Long commodityId) {
        LambdaQueryWrapper<Comments> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comments::getCustomerId, customerId);
        wrapper.eq(Comments::getOrderId, orderId);
        wrapper.eq(Comments::getCommodityId, commodityId);
        Comments one = this.getOne(wrapper);

        return one != null;
    }
}
