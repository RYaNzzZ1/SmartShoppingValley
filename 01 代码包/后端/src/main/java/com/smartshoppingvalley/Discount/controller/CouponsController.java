package com.smartshoppingvalley.Discount.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartshoppingvalley.Discount.dto.CouponsDto;
import com.smartshoppingvalley.Discount.entity.Coupons;
import com.smartshoppingvalley.Discount.entity.CouponsDetail;
import com.smartshoppingvalley.Discount.service.ICouponsDetailService;
import com.smartshoppingvalley.Discount.service.ICouponsService;
import com.smartshoppingvalley.tools.GetToken;
import com.smartshoppingvalley.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
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
@RequestMapping("/Discount/coupons")
public class CouponsController {

    @Autowired
    private ICouponsService couponsService;

    @Autowired
    private ICouponsDetailService couponsDetailService;

    @Autowired
    private HttpServletRequest request;

    //用户查看可领取的优惠券（未领取优惠券）
    @GetMapping("/list")
    public Result list() {
        log.info("用户查看可领取的优惠券");

        //TODO 获取当前登录用户id
//        Long customerId = 1L;
        String token = GetToken.getTokenFromRequest(request);
        String id = GetToken.getIdFromToken(token);
        Long customerId = Long.valueOf(id);

        //用户已领取优惠券
        List<Long> couponsIds = couponsDetailService.getIdsByCustomerId(customerId);

        //优惠券发布、处于有效期、按创建时间倒序、未领取
        LambdaQueryWrapper<Coupons> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Coupons::getStatus, 1);
        //按开始时间倒序排列
        wrapper.orderByDesc(Coupons::getStartTime);
        wrapper.le(Coupons::getStartTime, LocalDateTime.now());
        wrapper.ge(Coupons::getEndTime, LocalDateTime.now());
        wrapper.notIn(!couponsIds.isEmpty(), Coupons::getId, couponsIds);
        //按创建时间倒序
        //wrapper.orderByDesc(Coupons::getCreateTime);

        List<Coupons> couponsList = couponsService.list(wrapper);
        return Result.success(couponsList);
    }

    //用户领取优惠券
    @PostMapping("/receive")
    public Result receive(@RequestBody Coupons coupons) {
        log.info("用户领取优惠券");

        if (couponsService.getById(coupons.getId()) == null)
            return Result.error("该优惠券不存在，请尝试刷新页面后重试");

        //TODO 获取当前登录用户id
//        Long customerId = 1L;
        String token = GetToken.getTokenFromRequest(request);
        String id = GetToken.getIdFromToken(token);
        Long customerId = Long.valueOf(id);

        //领取优惠券
        CouponsDetail couponsDetail = new CouponsDetail();
        couponsDetail.setCouponsId(coupons.getId());
        couponsDetail.setCustomerId(customerId);
        couponsDetail.setCreateTime(LocalDateTime.now());
        couponsDetailService.save(couponsDetail);

        return Result.success();
    }

//    //用户使用优惠券
//    @PostMapping("/use")
//    public Result use(@RequestBody Coupons coupons) {
//        log.info("用户使用优惠券");
//
//        Long couponsId = coupons.getId();
//        //获取当前用户id
//        String tokenFromRequest = GetToken.getTokenFromRequest(request);
//        String customerId = GetToken.getIdFromToken(tokenFromRequest);
//
//        LambdaUpdateWrapper<CouponsDetail> wrapper = new LambdaUpdateWrapper<>();
//        wrapper.eq(CouponsDetail::getCustomerId, customerId);
//        wrapper.eq(CouponsDetail::getCouponsId, couponsId);
//        wrapper.set(CouponsDetail::getIsUsed, 1);
//        couponsDetailService.update(wrapper);
//
//        return Result.success();
//    }

    //用户查看领取的优惠券
    @GetMapping("/get")
    public Result get() {
        log.info("用户查看领取的优惠券");

        //返回数据
        List<CouponsDto> couponsDtoList = new ArrayList<>();

        //TODO 获取当前登录用户id
//        Long customerId = 1L;
        String token = GetToken.getTokenFromRequest(request);
        String id = GetToken.getIdFromToken(token);
        Long customerId = Long.valueOf(id);

        LambdaQueryWrapper<CouponsDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CouponsDetail::getCustomerId, customerId);
        wrapper.eq(CouponsDetail::getIsUsed, 0);
        //按领取时间倒序排列
        wrapper.orderByDesc(CouponsDetail::getCreateTime);
        List<CouponsDetail> detailList = couponsDetailService.list(wrapper);

        //未使用的优惠券id
        List<Long> unused = new ArrayList<>();

        //使用过的优惠券id
        List<Long> used = new ArrayList<>();
        for (CouponsDetail couponsDetail : detailList) {
            Long couponsDetailId = couponsDetail.getCouponsId();
            if (couponsDetail.getIsUsed() == 0) unused.add(couponsDetailId);
            else used.add(couponsDetailId);
        }

        for (Long l : unused) {
            CouponsDto couponsDto = new CouponsDto();
            Coupons coupons = couponsService.getById(l);
            if (coupons.getStartTime().isAfter(LocalDate.now()) || coupons.getEndTime().isBefore(LocalDate.now()))
                continue;

            BeanUtils.copyProperties(coupons, couponsDto);
            couponsDto.setIsUsed(0);
//
//            if(coupons.getEndTime().isAfter(LocalDate.now()))couponsDto.setIsExpired(0);
//            else couponsDto.setIsExpired(1);
            couponsDtoList.add(couponsDto);
        }
        for (Long l : used) {
            CouponsDto couponsDto = new CouponsDto();
            Coupons coupons = couponsService.getById(l);
            if (coupons.getStartTime().isAfter(LocalDate.now()) || coupons.getEndTime().isBefore(LocalDate.now()))
                continue;

            BeanUtils.copyProperties(coupons, couponsDto);
            couponsDto.setIsUsed(1);

//            if(coupons.getEndTime().isAfter(LocalDate.now()))couponsDto.setIsExpired(0);
//            else couponsDto.setIsExpired(1);
            couponsDtoList.add(couponsDto);
        }

//        //未领取过优惠券
//        if (ids.isEmpty()) return Result.success();
//
//        //领取过优惠券
//        LambdaQueryWrapper<Coupons> wrapper = new LambdaQueryWrapper<>();
//        wrapper.in(Coupons::getId, ids);
//        //按领取时间倒序排列
//        //wrapper.orderByDesc(Coupons::getCreateTime);
//        List<Coupons> couponsList = couponsService.list(wrapper);

        return Result.success(couponsDtoList);
    }

    //管理员分页查看优惠券
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, Integer> map) {
        int page = map.get("page");
        int limit = map.get("limit");
        log.info("管理员分页查看优惠券");

        Page<Coupons> couponsPage = new Page<>(page, limit);
        couponsService.page(couponsPage);
        return Result.success(couponsPage);
    }

    //管理员添加优惠券
    @PostMapping("/add")
    public Result add(@RequestBody Coupons coupons) {
        log.info("管理员添加优惠券：{}", coupons);

        coupons.setCreateTime(LocalDateTime.now());
        couponsService.save(coupons);

        return Result.success();
    }

    //管理员修改优惠券
    @Transactional
    @PutMapping("/update")
    public Result update(@RequestBody Coupons coupons) {
        log.info("管理员修改优惠券");

//        //修改后优惠券处于无效期，则删除领取记录
//        LocalDate now = LocalDate.now();
//        if (coupons.getStartTime().isAfter(now) || coupons.getEndTime().isBefore(now)) {
//            LambdaQueryWrapper<CouponsDetail> wrapper = new LambdaQueryWrapper<>();
//            wrapper.in(CouponsDetail::getCouponsId, coupons.getId());
//            couponsDetailService.remove(wrapper);
//        }

        couponsService.updateById(coupons);

        return Result.success();
    }

    //管理员批量修改优惠券状态
    @Transactional
    @PostMapping("/updateStatus/{status}")
    public Result updateStatus(@RequestBody Map<String, List<Long>> map, @PathVariable Integer status) {
        log.info("管理员批量修改优惠券状态");

        List<Long> ids = map.get("ids");

        //优惠券下架，删除领取记录
        if (status == 0) {
            LambdaQueryWrapper<CouponsDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(CouponsDetail::getCouponsId, ids);
            couponsDetailService.remove(wrapper);
        }

        //修改优惠券状态
        LambdaUpdateWrapper<Coupons> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Coupons::getId, ids);
        wrapper.set(Coupons::getStatus, status);
        couponsService.update(wrapper);

        return Result.success();
    }

    //管理员批量删除优惠券
    @Transactional
    @DeleteMapping("/delete")
    public Result delete(@RequestBody Map<String, List<Long>> map) {
        List<Long> ids = map.get("ids");

        log.info("管理员批量删除优惠券，ids = {}", ids);

        //状态为未发布才可以删除
        LambdaQueryWrapper<Coupons> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Coupons::getStatus, 1);
        wrapper.in(Coupons::getId, ids);
        List<Coupons> list = couponsService.list(wrapper);

        if (!list.isEmpty()) return Result.error("部分优惠券已发布，无法删除");

        couponsService.removeByIds(ids);

        return Result.success();
    }
}
