package com.smartshoppingvalley.Customer.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartshoppingvalley.Commodity.entity.Commodity;
import com.smartshoppingvalley.Commodity.service.ICommodityService;
import com.smartshoppingvalley.Customer.dto.ShoppingCartDto;
import com.smartshoppingvalley.Customer.entity.ShoppingCart;
import com.smartshoppingvalley.Customer.service.IShoppingCartService;
import com.smartshoppingvalley.ImageFile.service.IImageService;
import com.smartshoppingvalley.tools.GetToken;
import com.smartshoppingvalley.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/*
 * <p>
 * 购物车 前端控制器
 * </p>
 *

 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/Customer/shopping-cart")
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService shoppingCartService;

    @Autowired
    private ICommodityService commodityService;

    @Autowired
    private IImageService imageService;

    @Autowired
    private HttpServletRequest request;

    //添加商品到购物车
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCart shoppingCart) {
        log.info("添加商品到购物车");

        // TODO 获取用户id
//        Long customerId = 1L;
        String token = GetToken.getTokenFromRequest(request);
        String id = GetToken.getIdFromToken(token);
        Long customerId = Long.valueOf(id);

        shoppingCart.setCustomerId(customerId);

        //已添加相同商品，更改购物车表
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getCustomerId, shoppingCart.getCustomerId());
        wrapper.eq(ShoppingCart::getCommodityId, shoppingCart.getCommodityId());
        wrapper.eq(!shoppingCart.getCommodityAttribute().isEmpty(),
                ShoppingCart::getCommodityAttribute, shoppingCart.getCommodityAttribute());
        wrapper.isNull(shoppingCart.getCommodityAttribute().isEmpty(), ShoppingCart::getCommodityAttribute);
        ShoppingCart one = shoppingCartService.getOne(wrapper);
        if (one != null) {
            one.setNumber(shoppingCart.getNumber() + one.getNumber());
            one.setAmount(shoppingCart.getAmount().add(one.getAmount()));
            shoppingCartService.updateById(one);
            return Result.success();
        }

        //添加不同商品，添加购物车表

        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCartService.save(shoppingCart);

        return Result.success();
    }

    //修改购物车中的商品
    @PostMapping("/update")
    public Result update(@RequestBody ShoppingCart shoppingCart) {
        log.info("修改购物车中的商品");

        // TODO 获取用户id
//        Long customerId = 1L;
        String token = GetToken.getTokenFromRequest(request);
        String id = GetToken.getIdFromToken(token);
        Long customerId = Long.valueOf(id);

        shoppingCart.setCustomerId(customerId);

        //已添加相同商品，更改购物车表
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getCustomerId, shoppingCart.getCustomerId());
        wrapper.eq(ShoppingCart::getCommodityId, shoppingCart.getCommodityId());
        wrapper.eq(!shoppingCart.getCommodityAttribute().isEmpty(),
                ShoppingCart::getCommodityAttribute, shoppingCart.getCommodityAttribute());
        wrapper.isNull(shoppingCart.getCommodityAttribute().isEmpty(), ShoppingCart::getCommodityAttribute);
        ShoppingCart one = shoppingCartService.getOne(wrapper);
        if (one != null) {
            one.setNumber(shoppingCart.getNumber());
            one.setAmount(shoppingCart.getAmount());
            shoppingCartService.updateById(one);
            return Result.success();
        }

        //添加不同商品，添加购物车表

        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCartService.save(shoppingCart);

        return Result.success();
    }

    //查看购物车
    @GetMapping("/list")
    public Result list() {
        log.info("查看购物车");

        //返回数据
        List<ShoppingCartDto> shoppingCartDtoList = new ArrayList<>();

        //TODO 获取用户id
//        Long customerId = 1L;
        String token = GetToken.getTokenFromRequest(request);
        String id = GetToken.getIdFromToken(token);
        Long customerId = Long.valueOf(id);

        //查购物车表
        List<ShoppingCart> shoppingCartList = shoppingCartService.listByCustomerId(customerId);

//        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(ShoppingCart::getCustomerId, customerId);
//        //按时间倒序排列
//        //wrapper.orderByDesc(ShoppingCart::getCreateTime);
//
//        List<ShoppingCart> shoppingCartList = shoppingCartService.list(wrapper);

        for (ShoppingCart shoppingCart : shoppingCartList) {
            ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
            //获取商品信息
            Long commodityId = shoppingCart.getCommodityId();
            Commodity commodity = commodityService.getById(commodityId);
            BeanUtils.copyProperties(commodity, shoppingCartDto);

            //获取商品图片
            List<String> urls = imageService.getUrls(commodityId, 1);

//            LambdaQueryWrapper<Image> imageWrapper = new LambdaQueryWrapper<>();
//            imageWrapper.eq(Image::getAssociationId, commodity.getId());
//            imageWrapper.eq(Image::getType, 1);
//            List<Image> imageList = imageService.list(imageWrapper);
//
//            List<String> urls = new ArrayList<>();
//            for (Image image : imageList) {
//                urls.add(image.getUrl());
//            }

            shoppingCartDto.setUrls(urls);

            //获取购物车信息
            //BeanUtils.copyProperties(shoppingCart, shoppingCartDto, "id", "customerId", "commodityId");
            shoppingCartDto.setCommodityAttribute(shoppingCart.getCommodityAttribute());
            shoppingCartDto.setNumber(shoppingCart.getNumber());
            shoppingCartDto.setAmount(shoppingCart.getAmount());

            shoppingCartDtoList.add(shoppingCartDto);
        }

        return Result.success(shoppingCartDtoList);
    }

    //删除购物车商品
    @PostMapping("/delete")
    public Result delete(@RequestBody ShoppingCart shoppingCart) {
        log.info("删除购物车商品");

        //TODO 获取当前用户id
//        Long customerId = 1L;
        String token = GetToken.getTokenFromRequest(request);
        String id = GetToken.getIdFromToken(token);
        Long customerId = Long.valueOf(id);

        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getCustomerId, customerId);
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getCommodityId, shoppingCart.getCommodityId());
        shoppingCartLambdaQueryWrapper.isNull(shoppingCart.getCommodityAttribute().isEmpty(), ShoppingCart::getCommodityAttribute);
        shoppingCartLambdaQueryWrapper.eq(!shoppingCart.getCommodityAttribute().isEmpty(), ShoppingCart::getCommodityAttribute, shoppingCart.getCommodityAttribute());
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);

        return Result.success();
    }

    //清空购物车中商品
    @DeleteMapping("/clear")
    public Result clear() {
        log.info("清空购物车中商品");

        //TODO 获取用户id
//        Long customerId = 1L;
        String token = GetToken.getTokenFromRequest(request);
        String id = GetToken.getIdFromToken(token);
        Long customerId = Long.valueOf(id);

        shoppingCartService.clearByCustomerId(customerId);

        return Result.success();
    }

}
