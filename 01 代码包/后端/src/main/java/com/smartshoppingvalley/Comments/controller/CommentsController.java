package com.smartshoppingvalley.Comments.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartshoppingvalley.Comments.dto.CommentsDto;
import com.smartshoppingvalley.Comments.entity.Comments;
import com.smartshoppingvalley.Comments.service.ICommentsService;
import com.smartshoppingvalley.Customer.entity.CustomerDetail;
import com.smartshoppingvalley.Customer.service.ICustomerDetailService;
import com.smartshoppingvalley.Customer.service.ICustomerService;
import com.smartshoppingvalley.ImageFile.entity.Image;
import com.smartshoppingvalley.ImageFile.service.IImageService;
import com.smartshoppingvalley.tools.GetToken;
import com.smartshoppingvalley.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/Comments/comments")
public class CommentsController {

    @Autowired
    private ICommentsService commentsService;

    @Autowired
    private IImageService imageService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ICustomerDetailService customerDetailService;

    //获取某商品的平均得分
    @PostMapping("/getAverageScore")
    public Result getAverageScore(@RequestBody Map<String, Long> map) {
        log.info("获取某商品的平均得分");
        BigDecimal averageScore = new BigDecimal(0);

        Long commodityId = map.get("id");
        LambdaQueryWrapper<Comments> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comments::getCommodityId, commodityId);
        List<Comments> commentsList = commentsService.list(wrapper);

        //没有评论
        if (commentsList.isEmpty()) return Result.success(averageScore);

        for (Comments comments : commentsList) {
            averageScore = averageScore.add(BigDecimal.valueOf(comments.getScore()));
        }
        averageScore = averageScore.divide(BigDecimal.valueOf(commentsList.size()), 2, RoundingMode.HALF_UP);
        return Result.success(averageScore);
    }

    //查看某商品的所有评价（分页）
    @PostMapping("/pageByCommodityId")
    public Result pageByCommodityId(@RequestBody Map<String, Long> map) {
        log.info("查看某商品的评价");

        int page = Math.toIntExact((map.get("page")));
        int limit = Math.toIntExact(map.get("limit"));
        Long commodityId = map.get("commodityId");

        //返回数据
        Page<CommentsDto> commentsDtoPage = new Page<>();

        //查看评价记录
        Page<Comments> commentsPage = new Page<>(page, limit);
        LambdaQueryWrapper<Comments> commentsWrapper = new LambdaQueryWrapper<>();
        commentsWrapper.eq(Comments::getCommodityId, commodityId);
        //按照创建时间倒序排列
        //commentsWrapper.orderByDesc(Comments::getCreateTime);
        commentsService.page(commentsPage, commentsWrapper);

        BeanUtils.copyProperties(commentsPage, commentsDtoPage, "records");

        List<CommentsDto> commentsDtoList = new ArrayList<>();

        List<Comments> commentsList = commentsPage.getRecords();

        for (Comments comments : commentsList) {
            CommentsDto commentsDto = new CommentsDto();
            BeanUtils.copyProperties(comments, commentsDto);

            //获取发表评论用户名
            Long customerId = comments.getCustomerId();
            commentsDto.setUsername(customerService.getById(customerId).getUsername());

            //获取发表评论用户头像
            LambdaQueryWrapper<CustomerDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CustomerDetail::getCustomerId, customerId);
            commentsDto.setUrl(customerDetailService.getOne(wrapper).getAvatar());

            //获取评价图片
            List<String> urls = imageService.getUrls(comments.getId(), 2);

            commentsDto.setUrls(urls);
            commentsDtoList.add(commentsDto);
        }
        commentsDtoPage.setRecords(commentsDtoList);

        return Result.success(commentsDtoPage);
    }

    //添加评价
    @PostMapping("/add")
    @Transactional
    public Result add(@RequestBody CommentsDto commentsDto) {
        log.info("添加评价");

        //TODO 获取用户id
//        Long customerId = 1L;
        String token = GetToken.getTokenFromRequest(request);
        String id = GetToken.getIdFromToken(token);
        Long customerId = Long.valueOf(id);

        //保存评价
        LocalDateTime now = LocalDateTime.now();
        commentsDto.setCustomerId(customerId);
        commentsDto.setCreateTime(now);
        commentsService.save(commentsDto);

        //保存评价图片
        Long commentsDtoId = commentsDto.getId();

        List<String> urls = commentsDto.getUrls();
        List<Image> imageList = new ArrayList<>();

        for (String url : urls) {
            Image image = new Image();
            image.setAssociationId(commentsDtoId);
            image.setType(2);
            image.setUrl(url);
            image.setCreateTime(now);

            imageList.add(image);
        }

        imageService.saveBatch(imageList);
        return Result.success();
    }

    //删除评论（用户删评和管理员控评）
    @PostMapping("/delete")
    @Transactional
    public Result delete(@RequestBody Comments comments) {
        log.info("删除评价");

        Long commentsId = comments.getId();
        //删除评价
        commentsService.removeById(commentsId);

        //删除图片
        LambdaQueryWrapper<Image> imageWrapper = new LambdaQueryWrapper<>();
        imageWrapper.eq(Image::getType, 2);
        imageWrapper.eq(Image::getAssociationId, commentsId);
        imageService.remove(imageWrapper);

        return Result.success();

    }

    //管理员回复评论
    @PutMapping("/update")
    public Result update(@RequestBody Comments comments) {
        log.info("管理员回复评论");

        commentsService.updateById(comments);

        return Result.success();
    }

}
