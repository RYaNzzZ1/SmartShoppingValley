package com.smartshoppingvalley.Commodity.controller;


import com.smartshoppingvalley.Commodity.dto.CommodityDto;
import com.smartshoppingvalley.Commodity.entity.CommodityAttribute;
import com.smartshoppingvalley.Commodity.service.ICommodityAttributeService;
import com.smartshoppingvalley.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜品口味关系表 前端控制器
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/Commodity/attribute")
@CrossOrigin
public class CommodityAttributeController {
    @Autowired
    private ICommodityAttributeService iCommodityAttributeService;

    /*//添加商品属性
    @PostMapping("/addAttribute")
    public Result addAttribute(@RequestBody CommodityAttribute commodityAttribute)
    {
        log.info("添加商品属性");
        boolean result= iCommodityAttributeService.save(commodityAttribute);
        if(result)
            return Result.success();
        else
            return Result.error("添加失败");
    }*/
    //编辑商品属性
    @PostMapping("/editAttribute")
    public Result editAttribute(@RequestBody CommodityDto commodityDto) {
        log.info("编辑商品属性");
        List<CommodityAttribute> commodityAttributeList = commodityDto.getCommodityAttributeList();
        boolean result = true;

        if (commodityAttributeList.isEmpty()) {
            // 如果属性列表为空，可能需要根据实际需求处理异常情况
            return Result.error("属性列表为空");
        }
        long commodityId = commodityAttributeList.get(0).getCommodityId();

        if (!iCommodityAttributeService.deleteCommodityByCommodityId(commodityId))
            return Result.error("删除属性记录失败");
        /*if (!iCommodityAttributeService.removeById(commodityId)) {
            // 如果删除失败，可以根据实际需求进行处理或返回错误
            return Result.error("删除属性记录失败");
        }*/
        for (CommodityAttribute commodityAttribute : commodityAttributeList) {

            if (!iCommodityAttributeService.save(commodityAttribute)) {
                result = false;
            }
        }

        if (result) {
            return Result.success();
        } else {
            return Result.error("更新失败");
        }
    }
}
