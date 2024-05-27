package com.smartshoppingvalley.Commodity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartshoppingvalley.Commodity.dto.CommodityDto;
import com.smartshoppingvalley.Commodity.entity.Commodity;
import com.smartshoppingvalley.Commodity.mapper.CommodityMapper;
import com.smartshoppingvalley.Commodity.service.ICategoryService;
import com.smartshoppingvalley.Commodity.service.ICommodityService;
import com.smartshoppingvalley.ImageFile.service.IImageService;
import com.smartshoppingvalley.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 菜品管理 服务实现类
 * </p>
 */
@Service
@Slf4j
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements ICommodityService {
    @Autowired
    private CommodityMapper commodityMapper;
    @Autowired
    private ICommodityService iCommodityService;
    @Autowired
    private IImageService iImageService;
    @Autowired
    private ICategoryService iCategoryService;

    public int sell_commodity(@RequestParam long id, @RequestParam Integer num) {
        UpdateWrapper<Commodity> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id);
        Commodity commodity = this.getOne(wrapper);
        Integer number = commodity.getNum() - num;
        commodity.setNum(number);
        return commodityMapper.update(commodity, wrapper);
    }

    public int delist_commodity(long id) {
        UpdateWrapper<Commodity> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id);
        Commodity commodity = this.getOne(wrapper);
        if (commodity.getStatus() == 1)
            commodity.setStatus(0);
        else if (commodity.getStatus() == 0)
            commodity.setStatus(1);

        return commodityMapper.update(commodity, wrapper);
    }

    public Result screen_commodity(long categoryId, int p, int size) {
        QueryWrapper<Commodity> wrapper = new QueryWrapper<>();
        wrapper.like("category_id", categoryId);
        Page<Commodity> page = new Page<>(p, size);
        Page<Commodity> commodityPage = commodityMapper.selectPage(page, wrapper);
        return Result.success(commodityPage);
    }

    @Override
    public Result get_commodity(int page, int limit) {
        Page<Commodity> commodityPage = new Page<>(page, limit);

        //返回数据
        Page<CommodityDto> commodityDtoPage = new Page<>();
        List<CommodityDto> commodityDtoList = new ArrayList<>();

        this.page(commodityPage);

        BeanUtils.copyProperties(commodityPage, commodityDtoPage, "records");

        for (Commodity record : commodityPage.getRecords()) {
            CommodityDto commodityDto = CommodityDto.fromCommodity(record);

            List<String> urls = iImageService.getUrls(record.getId(), 1);

            if (!urls.isEmpty())
                commodityDto.setUrls(Collections.singletonList(urls.get(0)));
            commodityDtoList.add(commodityDto);

        }
        commodityDtoPage.setRecords(commodityDtoList);
        return Result.success(commodityDtoPage);
    }

//    @Override
//    public Result get_commodity(int page, int limit) {
//        List<CommodityDto>commodityDtoList=new ArrayList<>();
//        List<Commodity> commodityList=iCommodityService.list();
//        for(Commodity commodity:commodityList)
//        {
//            CommodityDto commodityDto = new CommodityDto();
//            BeanUtils.copyProperties(commodity, commodityDto);
//            List<String> urls = iImageService.getUrls(commodity.getId(),1);
//            if (urls != null && !urls.isEmpty()) {
//                String url = urls.get(0);
//                commodityDto.getUrls().add(url);
//            }
//
//            commodityDtoList.add(commodityDto);
//        }
//        List<CommodityDtoss> commodityDtossList =new ArrayList<>();
//        for(CommodityDto commodityDto:commodityDtoList)
//        {
//            CommodityDtoss commodityDtoss =new CommodityDtoss();
//            long categoryId=commodityDto.getCategoryId();
//            QueryWrapper<Category> categoryQueryWrapper=new QueryWrapper<>();
//            categoryQueryWrapper.eq("id",categoryId);
//            Category category=iCategoryService.getOne(categoryQueryWrapper);
//            long fatherId=category.getFatherId();
//            if(fatherId!=-1) {
//                BeanUtils.copyProperties(commodityDto, commodityDtoss);
//                commodityDtoss.setFatherId(fatherId);
//                commodityDtossList.add(commodityDtoss);
//            }
//        }
//        new Page<>(page, limit);
//        int startIndex = (page - 1) * limit;
//        int endIndex = Math.min(startIndex + limit, commodityDtossList.size());
//        List<CommodityDtoss> currentPageCommodityList = commodityDtossList.subList(startIndex, endIndex);
//        Page<CommodityDtoss> commodityPage = new Page<>(page, limit);
//        commodityPage.setRecords(currentPageCommodityList);
//        commodityPage.setTotal(commodityDtossList.size());
//        return Result.success(commodityPage);
//    }
    /*QueryWrapper<Commodity> wrapper = new QueryWrapper<>();
        commodityMapper.selectList(wrapper);
    Page<Commodity> pages = new Page(page,limit);
    Page<Commodity> commodityPage=commodityMapper.selectPage(pages,wrapper);*/

}
