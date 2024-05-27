package com.smartshoppingvalley.Commodity.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartshoppingvalley.Commodity.dto.CommodityAttributeDto;
import com.smartshoppingvalley.Commodity.dto.CommodityDto;
import com.smartshoppingvalley.Commodity.dto.CommodityDtoss;
import com.smartshoppingvalley.Commodity.entity.Category;
import com.smartshoppingvalley.Commodity.entity.Commodity;
import com.smartshoppingvalley.Commodity.entity.CommodityAttribute;
import com.smartshoppingvalley.Commodity.mapper.CommodityMapper;
import com.smartshoppingvalley.Commodity.service.ICategoryService;
import com.smartshoppingvalley.Commodity.service.ICommodityAttributeService;
import com.smartshoppingvalley.Commodity.service.ICommodityService;
import com.smartshoppingvalley.ImageFile.entity.Image;
import com.smartshoppingvalley.ImageFile.service.IImageService;
import com.smartshoppingvalley.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 菜品管理 前端控制器
 * </p>
 */
@RestController
@Slf4j
@RequestMapping("/Commodity/commodity")
@CrossOrigin
public class CommodityController {
    @Autowired
    private ICommodityService iCommodityService;
    @Autowired
    private CommodityMapper commodityMapper;
    @Autowired
    private IImageService iImageService;
    @Autowired
    private ICommodityAttributeService iCommodityAttributeService;
    @Autowired
    private ICategoryService iCategoryService;

    //添加商品
    @PostMapping("/addGoods")
    public Result addGoods(@RequestBody CommodityDto commodityDto) {
        log.info("添加商品");
        LocalDateTime now = LocalDateTime.now();
        commodityDto.setIsDeleted(0);
        commodityDto.setCreateTime(now);
        boolean result = iCommodityService.save(commodityDto);


        List<String> urls = commodityDto.getUrls();
        List<Image> imageList = new ArrayList<>();
        for (String url : urls) {
            Image image = new Image();
            image.setType(1);
            image.setAssociationId(commodityDto.getId());
            image.setUrl(url);
            image.setCreateTime(now);
            imageList.add(image);
        }
        iImageService.saveBatch(imageList);

        List<CommodityAttribute> commodityAttributeList = commodityDto.getCommodityAttributeList();

        List<CommodityAttribute> commodityAttributes = new ArrayList<>();

        for (CommodityAttribute commodityAttribute : commodityAttributeList) {
            if (commodityAttribute.getName().isEmpty() || commodityAttribute.getValue().isEmpty()) {
                continue;
            }
            commodityAttributes.add(commodityAttribute.setCommodityId(commodityDto.getId()));
        }
        iCommodityAttributeService.saveBatch(commodityAttributes);

        if (result)
            return Result.success();
        else
            return Result.error("添加失败");

    }

    @PostMapping("/delistGoods")
    public Result delistGoods(@RequestParam Long id) {
        int result = iCommodityService.delist_commodity(id);
        if (result == 1)
            return Result.success();
        else
            return Result.error("下架失败");
    }

    /*@PostMapping("/update")
    public Result update_commodity(@RequestBody Commodity commodity)
    {
        boolean result=iCommodityService.updateById(commodity);
        if(result)
            return Result.success();
        else
            return Result.error("更新失败");
    }
    @PostMapping("/sell")
    public Result sell_commodity(@RequestBody Map<String, String> map)
    {
        long id = Long.parseLong(map.get("id"));
        String num = map.get("num");
        int result=iCommodityService.sell_commodity(id, Integer.valueOf(num));
        if(result==1)
            return Result.success();
        else
            return Result.error("出售失败");
    }*/
    //获取商品详细信息
    @PostMapping("/getGoodsDetail")
    public Result getGoodsDetail(@RequestBody Commodity commodity) {
        log.info("获取商品详细信息");
        Long commodityId = commodity.getId();

        //返回数据
        CommodityAttributeDto commodityAttributeDto = new CommodityAttributeDto();

        commodity = iCommodityService.getById(commodityId);
        BeanUtils.copyProperties(commodity, commodityAttributeDto);

        List<String> urls = iImageService.getUrls(commodityId, 1);
        commodityAttributeDto.setUrls(urls);

        LambdaQueryWrapper<CommodityAttribute> commodityAttributeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commodityAttributeLambdaQueryWrapper.eq(CommodityAttribute::getCommodityId, commodityId);
        List<CommodityAttribute> commodityAttributeList = iCommodityAttributeService.list(commodityAttributeLambdaQueryWrapper);

        List<Map<String, Object>> mapList = new ArrayList<>();
        for (CommodityAttribute commodityAttribute : commodityAttributeList) {
            String name = commodityAttribute.getName();
            String value = commodityAttribute.getValue();

            Map<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("values", value.split(","));

            mapList.add(map);
        }
        commodityAttributeDto.setCommodityAttributeList(mapList);
        return Result.success(commodityAttributeDto);


//        CommodityDto commodityDto = CommodityDto.fromCommodity(iCommodityService.getById(id));
//        commodityDto.setCommodityAttributeList(iCommodityAttributeService.getAttribute(id));
//        commodityDto.setUrls(iImageService.getUrls(id, 1));
//        CommodityDtos commodityDtos = CommodityDtos.fromCommodity(commodityDto);
//        QueryWrapper<Collections> wrapper = new QueryWrapper<>();
//        wrapper.eq("customer_id", 1);
//        wrapper.eq("commodity_id", id);
//        Collections collections = iCollectionService.getOne(wrapper);
//        if (collections != null)
//            commodityDtos.setIsCollected(1);
//        else
//            commodityDtos.setIsCollected(0);
//        //TODO 获取用户id
//        if (commodityDtos.getIsDeleted() != 1)
//            return Result.success(commodityDtos);
//        else
//            return Result.error("显示失败");
    }

    //获取商品列表(分页)
    @PostMapping("/getGoods")
    public Result getGoods(Integer page, Integer limit) {
        log.info("获取商品列表（分页）");
        List<CommodityDto> commodityDtoList = new ArrayList<>();

        if (page != null && limit != null) {
            return iCommodityService.get_commodity(page, limit);
        } else {
            List<Commodity> commodityList = iCommodityService.list();
            for (Commodity commodity : commodityList) {
                CommodityDto commodityDto = new CommodityDto();
                BeanUtils.copyProperties(commodity, commodityDto);
                List<String> urls = iImageService.getUrls(commodity.getId(), 1);
                if (urls != null && !urls.isEmpty()) {
                    String url = urls.get(0);
                    commodityDto.getUrls().add(url);
                }
                commodityDtoList.add(commodityDto);
            }
            List<CommodityDtoss> commodityDtossList = new ArrayList<>();
            for (CommodityDto commodityDto : commodityDtoList) {
                CommodityDtoss commodityDtoss = new CommodityDtoss();
                long categoryId = commodityDto.getCategoryId();
                QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
                categoryQueryWrapper.eq("id", categoryId);
                Category category = iCategoryService.getOne(categoryQueryWrapper);
                long fatherId = category.getFatherId();
                if (fatherId != -1) {
                    BeanUtils.copyProperties(commodityDto, commodityDtoss);
                    commodityDtoss.setFatherId(fatherId);
                    commodityDtossList.add(commodityDtoss);
                }
            }
            if (!commodityDtossList.isEmpty())
                return Result.success(commodityDtossList);
            else
                return Result.error("找不到商品，获取失败");
        }

    }

    /*@PostMapping("/screenbycategory")
    public Result screen_commodity(@RequestParam long categoryId,@RequestParam int p,@RequestParam int size)
    {
        return iCommodityService.screen_commodity(categoryId,p,size);
    }*/
    //根据分类id获取商品列表(分页/不分页)
    @PostMapping("/getCommodityByCategoryId")
    public Result getCommodityByCategoryId(@RequestParam long categoryId, Integer page, Integer limit) {
        log.info("根据分类id获取商品列表(分页/不分页)");
        /*QueryWrapper<Commodity> wrapper = new QueryWrapper<>();
        wrapper.eq("category_id",categoryId);*/
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.eq("id", categoryId);
        Category category = iCategoryService.getOne(categoryQueryWrapper);
        if (category.getFatherId() == -1) {
            List<Long> categoryList = new ArrayList<>();
            List<Category> categories = iCategoryService.list();
            for (Category category1 : categories) {
                if (category1.getFatherId() == categoryId)
                    categoryList.add(category1.getId());
            }
            log.info(categoryList.toString());
            QueryWrapper<Commodity> commodityQueryWrapper = new QueryWrapper<>();
            List<CommodityDto> commodityDtoList = new ArrayList<>();

            List<Commodity> commodityList = new ArrayList<>();
            for (long id : categoryList) {
                commodityQueryWrapper.eq("category_id", id);
                List<Commodity> subList = iCommodityService.list(commodityQueryWrapper);
                commodityList.addAll(subList);
                commodityQueryWrapper = new QueryWrapper<>();
            }

            for (Commodity commodity : commodityList) {
                CommodityDto commodityDto = new CommodityDto();
                BeanUtils.copyProperties(commodity, commodityDto);
                List<String> urls = iImageService.getUrls(commodity.getId(), 1);
                if (urls != null && !urls.isEmpty()) {
                    String url = urls.get(0);
                    commodityDto.getUrls().add(url);
                }
                commodityDtoList.add(commodityDto);
            }
            List<CommodityDtoss> commodityDtossList = new ArrayList<>();
            for (CommodityDto commodityDto : commodityDtoList) {
                CommodityDtoss commodityDtoss = new CommodityDtoss();
                BeanUtils.copyProperties(commodityDto, commodityDtoss);
                commodityDtoss.setFatherId(categoryId);
                commodityDtossList.add(commodityDtoss);
            }
            if (page != null && limit != null) {
                new Page<>(page, limit);
                int startIndex = (page - 1) * limit;
                int endIndex = Math.min(startIndex + limit, commodityDtossList.size());
                List<CommodityDtoss> currentPageCommodityList = commodityDtossList.subList(startIndex, endIndex);
                Page<CommodityDtoss> commodityPage = new Page<>(page, limit);
                commodityPage.setRecords(currentPageCommodityList);
                commodityPage.setTotal(commodityDtossList.size());
                return Result.success(commodityPage);
            } else {
                if (!commodityDtossList.isEmpty())
                    return Result.success(commodityDtossList);
                else
                    return Result.error("找不到商品，获取失败");
            }
        } else {
            QueryWrapper<Commodity> wrapper = new QueryWrapper<>();
            wrapper.eq("category_id", categoryId);
            List<Commodity> commodityList = iCommodityService.list(wrapper);
            List<CommodityDto> commodityDtoList = new ArrayList<>();
            for (Commodity commodity : commodityList) {
                CommodityDto commodityDto = new CommodityDto();
                BeanUtils.copyProperties(commodity, commodityDto);
                List<String> urls = iImageService.getUrls(commodity.getId(), 1);
                if (urls != null && !urls.isEmpty()) {
                    String url = urls.get(0);
                    commodityDto.getUrls().add(url);
                }
                commodityDtoList.add(commodityDto);
            }
            List<CommodityDtoss> commodityDtossList = new ArrayList<>();
            for (CommodityDto commodityDto1 : commodityDtoList) {
                CommodityDtoss commodityDtoss = new CommodityDtoss();
                BeanUtils.copyProperties(commodityDto1, commodityDtoss);
                commodityDtoss.setFatherId(category.getFatherId());
                commodityDtossList.add(commodityDtoss);
            }

            if (page != null && limit != null) {
                Page<CommodityDtoss> pages = new Page<>(page, limit);
                int startIndex = (page - 1) * limit;
                int endIndex = Math.min(startIndex + limit, commodityDtossList.size());
                List<CommodityDtoss> currentPageCommodityList = commodityDtossList.subList(startIndex, endIndex);
                Page<CommodityDtoss> commodityPage = new Page<>(page, limit);
                commodityPage.setRecords(currentPageCommodityList);
                commodityPage.setTotal(commodityDtossList.size());
                return Result.success(commodityPage);
            } else {
                if (!commodityDtossList.isEmpty())
                    return Result.success(commodityDtossList);
                else
                    return Result.error("找不到商品，获取失败");
            }
        }
    }

    //根据id，分类id，名字，状态搜索商品
    @PostMapping("/searchGoods")
    public Result searchGoods(@RequestBody Map<String, String> map) {

        Long id = null;
        Long categoryId = null;
        String name = null;
        Integer status = null;
//        int page = Integer.parseInt(map.get("page"));
//        int limit = Integer.parseInt(map.get("limit"));

        //返回数据
//        Page<CommodityDto> commodityDtoPage = new Page<>();
        List<CommodityDto> commodityDtoList = new ArrayList<>();

        if (map.get("id") != null && !map.get("id").isEmpty()) id = Long.valueOf(map.get("id"));
        if (map.get("categoryId") != null && !map.get("categoryId").isEmpty())
            categoryId = Long.valueOf(map.get("categoryId"));
        if (map.get("name") != null && !map.get("name").isEmpty()) name = (map.get("name"));
        if (map.get("status") != null && !map.get("status").isEmpty()) status = Integer.valueOf(map.get("status"));

        log.info("根据id，分类id，名字，状态搜索商品");
        // 构建查询条件

        LambdaQueryWrapper<Commodity> commodityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commodityLambdaQueryWrapper.eq(id != null, Commodity::getId, id);
        commodityLambdaQueryWrapper.eq(categoryId != null, Commodity::getCategoryId, categoryId);
        commodityLambdaQueryWrapper.like(name != null, Commodity::getName, name);
        commodityLambdaQueryWrapper.eq(status != null, Commodity::getStatus, status);

//        Page<Commodity> commodityPage = new Page<>(page, limit);
        List<Commodity> commodityList = iCommodityService.list(commodityLambdaQueryWrapper);
//        BeanUtils.copyProperties(commodityPage, commodityDtoPage, "records");

        for (Commodity record : commodityList) {
            CommodityDto commodityDto = CommodityDto.fromCommodity(record);
            List<String> urls = iImageService.getUrls(record.getId(), 1);
            if (!urls.isEmpty()) commodityDto.setUrls(Collections.singletonList(urls.get(0)));
            commodityDtoList.add(commodityDto);
        }
//        commodityDtoPage.setRecords(commodityDtoList);
        return Result.success(commodityDtoList);

//        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
//        if (id != null) {
//            queryWrapper.eq("id", id);
//        }
//
//        if (name != null) {
//            queryWrapper.like("name", name);
//        }
//        if (status != null) {
//            queryWrapper.eq("status", status);
//        }
//        if (categoryId != null) {
//            QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
//            categoryQueryWrapper.eq("id", categoryId);
//            Category category = iCategoryService.getOne(categoryQueryWrapper);
//            long fatherId = category.getFatherId();
//            if (fatherId != -1) {
//                queryWrapper.eq("category_id", categoryId);
//            } else {
//                List<Category> categoryList = iCategoryService.list();
//                List<Long> categoryIds = new ArrayList<>();
//                for (Category categorys : categoryList) {
//                    if (categorys.getFatherId() == fatherId)
//                        categoryIds.add(categorys.getId());
//                }
//                queryWrapper.in("id", categoryIds);
//            }
//        }
//        List<CommodityDto> commodityDtoList = new ArrayList<>();
//        List<Commodity> commodityList = iCommodityService.list(queryWrapper);
//        for (Commodity commodity : commodityList) {
//            CommodityDto commodityDto = new CommodityDto();
//            BeanUtils.copyProperties(commodity, commodityDto);
//            List<String> urls = iImageService.getUrls(commodity.getId(), 1);
//            if (urls != null && !urls.isEmpty()) {
//                String url = urls.get(0);
//                commodityDto.getUrls().add(url);
//            }
//            commodityDtoList.add(commodityDto);
//        }
//        int startIndex = (page - 1) * limit;
//        int endIndex = Math.min(startIndex + limit, commodityDtoList.size());
//        List<CommodityDto> currentPageCommodityList = commodityDtoList.subList(startIndex, endIndex);
//        Page<CommodityDto> commodityPage = new Page<>(page, limit);
//        commodityPage.setRecords(currentPageCommodityList);
//        commodityPage.setTotal(commodityDtoList.size());
//        return Result.success(commodityPage);

    }

    //编辑商品
    @PostMapping("/editGoods")
    public Result editGoods(@RequestBody CommodityDto commodityDto) {
        log.info("编辑商品");
        iCommodityService.updateById(commodityDto);
        List<CommodityAttribute> commodityAttributeList = commodityDto.getCommodityAttributeList();

        for (CommodityAttribute commodityAttribute : commodityAttributeList) {
            commodityAttribute.setCommodityId(commodityDto.getId());
        }
        iCommodityAttributeService.deleteCommodityByCommodityId(commodityDto.getId());
        iCommodityAttributeService.saveBatch(commodityAttributeList);
        List<String> urls = commodityDto.getUrls();
        iImageService.updateUrls(urls, commodityDto.getId(), 1);
        return Result.success();


    }

}
