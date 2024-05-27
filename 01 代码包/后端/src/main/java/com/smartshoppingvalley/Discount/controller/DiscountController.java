package com.smartshoppingvalley.Discount.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartshoppingvalley.Commodity.dto.CommodityDto;
import com.smartshoppingvalley.Commodity.entity.Commodity;
import com.smartshoppingvalley.Commodity.service.ICommodityService;
import com.smartshoppingvalley.Discount.entity.Discount;
import com.smartshoppingvalley.Discount.entity.DiscountDetail;
import com.smartshoppingvalley.Discount.service.IDiscountDetailService;
import com.smartshoppingvalley.Discount.service.IDiscountService;
import com.smartshoppingvalley.ImageFile.service.IImageService;
import com.smartshoppingvalley.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
@RequestMapping("/Discount/discount")
public class DiscountController {

    @Autowired
    private IDiscountService discountService;

    @Autowired
    private IDiscountDetailService discountDetailService;

    @Autowired
    private ICommodityService commodityService;

    @Autowired
    private IImageService imageService;

    //通用：分页查看折扣
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, Integer> map) {
        log.info("分页查看折扣");
        int page = map.get("page");
        int limit = map.get("limit");

        Page<Discount> discountPage = new Page<>(page, limit);
        LambdaQueryWrapper<Discount> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Discount::getStartTime);
        wrapper.orderByDesc(Discount::getEndTime);
        discountService.page(discountPage);
        return Result.success(discountPage);
    }

    //查看参与折扣的商品（分页）
    @PostMapping("/pageByDiscountId")
    public Result pageByDiscountId(@RequestBody Map<String, Long> map) {
        log.info("查看参与折扣的商品（分页）");

        //返回数据
        Page<CommodityDto> commodityDtoPage = new Page<>();

        int page = Math.toIntExact(map.get("page"));
        int limit = Math.toIntExact(map.get("limit"));
        Long discountId = map.get("discountId");

        Discount discount = discountService.getById(discountId);
        BigDecimal value = discount.getValue();

        Page<DiscountDetail> discountDetailPage = new Page<>(page, limit);
        LambdaQueryWrapper<DiscountDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DiscountDetail::getDiscountId, discountId);
        discountDetailService.page(discountDetailPage, wrapper);
        BeanUtils.copyProperties(discountDetailPage, commodityDtoPage, "records");

        List<CommodityDto> commodityDtoList = new ArrayList<>();
        for (DiscountDetail record : discountDetailPage.getRecords()) {
            Long commodityId = record.getCommodityId();
            CommodityDto commodityDto = CommodityDto.fromCommodity(commodityService.getById(commodityId));

            //折扣价格
            commodityDto.setPrice(commodityDto.getPrice().multiply(value));

            List<String> urls = imageService.getUrls(commodityId, 1);
            commodityDto.setUrls(urls);
            commodityDtoList.add(commodityDto);
        }
        commodityDtoPage.setRecords(commodityDtoList);

        return Result.success(commodityDtoPage);
    }

    //管理员添加折扣
    @PostMapping("/add")
    public Result add(@RequestBody Discount discount) {
        log.info("管理员添加优惠券：{}", discount);

        discount.setCreateTime(LocalDateTime.now());
        discountService.save(discount);

        return Result.success();
    }

    //管理员查看未参与折扣商品（用于添加折扣）
    @PostMapping("/pageCommodityWithoutDiscount")
    public Result pageCommodityWithoutDiscount(@RequestBody Map<String, Integer> map) {
        log.info("管理员查看未参与折扣商品（用于添加折扣）");
        int page = map.get("page");
        int limit = map.get("limit");

        //返回数据
        Page<CommodityDto> commodityDtoPage = new Page<>();

        Page<Commodity> commodityPage = new Page<>(page, limit);

        //获取参与折扣的所有商品id
        List<Long> ids = discountDetailService.allCommodityIdsWithDiscount();

        //商品基础信息
        LambdaQueryWrapper<Commodity> commodityWrapper = new LambdaQueryWrapper<>();
        commodityWrapper.notIn(!ids.isEmpty(), Commodity::getId, ids);
        commodityService.page(commodityPage, commodityWrapper);

        BeanUtils.copyProperties(commodityPage, commodityDtoPage, "records");

        List<CommodityDto> commodityDtoList = new ArrayList<>();
        for (Commodity commodity : commodityPage.getRecords()) {
            CommodityDto commodityDto = CommodityDto.fromCommodity(commodity);

            //商品图片信息
            List<String> urls = imageService.getUrls(commodity.getId(), 1);

            commodityDto.setUrls(urls);
            commodityDtoList.add(commodityDto);
        }
        commodityDtoPage.setRecords(commodityDtoList);

        return Result.success(commodityDtoPage);
    }

    //管理员设置折扣商品（批量操作）
    @PostMapping("/addCommodity/{discountId}")
    public Result addCommodity(@PathVariable Long discountId, @RequestBody Map<String, List<Long>> map) {
        log.info("管理员设置折扣商品（批量操作）");

        List<Long> commodityIds = map.get("commodityIds");
        LocalDateTime now = LocalDateTime.now();

        //折扣详情列表
        List<DiscountDetail> list = new ArrayList<>();
        for (Long commodityId : commodityIds) {
            DiscountDetail discountDetail = new DiscountDetail();
            discountDetail.setDiscountId(discountId);
            discountDetail.setCommodityId(commodityId);
            discountDetail.setCreateTime(now);

            list.add(discountDetail);
        }

        discountDetailService.saveBatch(list);
        return Result.success();
    }


    //管理员删除折扣商品（批量操作）
    @PostMapping("/deleteCommodity/{discountId}")
    public Result deleteCommodity(@PathVariable Long discountId, @RequestBody Map<String, List<Long>> map) {
        log.info("管理员删除折扣商品（批量操作）");
        List<Long> commodityIds = map.get("commodityIds");

        LambdaQueryWrapper<DiscountDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DiscountDetail::getDiscountId, discountId);
        wrapper.in(DiscountDetail::getCommodityId, commodityIds);
        discountDetailService.remove(wrapper);

        return Result.success();
    }


    //管理员修改折扣
    @Transactional
    @PutMapping("/update")
    public Result update(@RequestBody Discount discount) {
        log.info("管理员修改折扣");

        discountService.updateById(discount);
        return Result.success();
    }

    //管理员批量修改折扣状态
    @Transactional
    @PutMapping("/updateStatus/{status}")
    public Result updateStatus(@RequestParam List<Long> ids, @PathVariable Integer status) {
        log.info("管理员批量修改折扣状态");

        //优惠券下架，删除打折商品记录
        if (status == 0) {
            LambdaQueryWrapper<DiscountDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(DiscountDetail::getDiscountId, ids);
            discountDetailService.remove(wrapper);
        }

        //修改优惠券状态
        LambdaUpdateWrapper<Discount> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Discount::getId, ids);
        wrapper.set(Discount::getStatus, status);
        discountService.update(wrapper);

        return Result.success();
    }

    //管理员批量删除折扣
    @Transactional
    @DeleteMapping("/delete")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("管理员批量删除优惠券，ids = {}", ids);

        //状态为未发布才可以删除
        LambdaQueryWrapper<Discount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Discount::getStatus, 1);
        wrapper.in(Discount::getId, ids);
        List<Discount> list = discountService.list(wrapper);

        if (!list.isEmpty()) return Result.error("部分优惠券已发布，无法删除");

        discountService.removeByIds(ids);

        return Result.success();
    }
}
