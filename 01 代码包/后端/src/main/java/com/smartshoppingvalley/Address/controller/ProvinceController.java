package com.smartshoppingvalley.Address.controller;


import com.smartshoppingvalley.Address.entity.Province;
import com.smartshoppingvalley.Address.service.IProvinceService;
import com.smartshoppingvalley.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/Address/province")
public class ProvinceController {

    @Autowired
    private IProvinceService provinceService;

    //获取省份列表
    @GetMapping("/list")
    public Result list() {
        log.info("获取省份列表");

        List<Province> provinceList = provinceService.list();

        return Result.success(provinceList);
    }
}
