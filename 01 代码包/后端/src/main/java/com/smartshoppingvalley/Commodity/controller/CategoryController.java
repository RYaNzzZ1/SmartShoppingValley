package com.smartshoppingvalley.Commodity.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartshoppingvalley.Commodity.entity.Category;
import com.smartshoppingvalley.Commodity.entity.Commodity;
import com.smartshoppingvalley.Commodity.service.ICategoryService;
import com.smartshoppingvalley.Commodity.service.ICommodityService;
import com.smartshoppingvalley.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 菜品及套餐分类 前端控制器
 * </p>
 */
@RestController
@Slf4j
@RequestMapping("/Commodity/type")
@CrossOrigin
public class CategoryController {
    @Autowired
    private ICategoryService iCategoryService;
    @Autowired
    private ICommodityService iCommodityService;

    public CategoryController() {
    }

    @PostMapping("/show")
    public Result show_category() {
        List<Category> categories = iCategoryService.list();
        return Result.success(categories);
    }

    //添加商品类型
    @PostMapping("/addType")
    public Result addType(@RequestParam String name, @RequestParam Long fatherId) {
        log.info("添加商品类型");
        Category category = new Category(fatherId, name);
        category.setCreateTime(LocalDateTime.now());
        boolean result = iCategoryService.save(category);
        if (result)
            return Result.success();
        else
            return Result.error("添加失败");
    }

    //编辑商品类型
    @PostMapping("/editType")
    public Result editType(@RequestParam String name, @RequestParam long id) {
        log.info("编辑商品类型");
        Category category = iCategoryService.getById(id);
        category.setName(name);
        boolean result = iCategoryService.updateById(category);
        if (result)
            return Result.success();
        else
            return Result.error("添加失败");
    }

    //删除商品类型
    @PostMapping("/deleteType")
    public Result deleteType(@RequestParam long id) {
        log.info("删除商品类型");
        List<Category> categories = iCategoryService.list();
        for (Category category : categories) {
            if (category.getFatherId() == id)
                return Result.error("该类存在子类，删除失败");
        }
        List<Commodity> commodities = iCommodityService.list();
        for (Commodity commodity : commodities) {
            if (commodity.getCategoryId() == id)
                return Result.error("该类还有商品，删除失败");
        }
        boolean result = iCategoryService.removeById(id);
        if (result)
            return Result.success();
        else
            return Result.error("删除失败");
    }

    //根据父类id搜索类型
    @PostMapping("/getTypeByFatherId")
    public Result getTypeByFatherId(@RequestBody Category category) {
        log.info("根据父类id搜索类型");
        //log.info(fatherId.toString());
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("father_id", category.getFatherId());
        List<Category> categoryList = iCategoryService.list(wrapper);
        if (!categoryList.isEmpty())
            return Result.success(categoryList);
        else
            return Result.error("找不到该类别");
    }
}
