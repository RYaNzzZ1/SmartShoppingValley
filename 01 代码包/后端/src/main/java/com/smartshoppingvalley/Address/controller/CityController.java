package com.smartshoppingvalley.Address.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartshoppingvalley.Address.entity.City;
import com.smartshoppingvalley.Address.service.ICityService;
import com.smartshoppingvalley.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/Address/city")
public class CityController {

    @Autowired
    private ICityService cityService;

    //获取省份城市列表
    @GetMapping("/listByProvinceId")
    public Result list(@RequestParam Integer provinceId) {
        log.info("获取省份城市列表，provinceId = {}", provinceId);

        LambdaQueryWrapper<City> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(provinceId != null, City::getProvinceId, provinceId);
        List<City> cityList = cityService.list(wrapper);

        return Result.success(cityList);
    }

}
