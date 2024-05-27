package com.smartshoppingvalley.ImageFile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartshoppingvalley.ImageFile.entity.Image;
import com.smartshoppingvalley.ImageFile.mapper.ImageMapper;
import com.smartshoppingvalley.ImageFile.service.IImageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements IImageService {
    public List<String> getUrls(long associationId, int type) {
        QueryWrapper<Image> wrapper = new QueryWrapper<>();
        wrapper.eq("association_id", associationId);
        wrapper.eq("type", type);
        List<Image> imageList = this.list(wrapper);
        List<String> urlsList = new ArrayList<>();
        for (Image images : imageList) {
            urlsList.add(images.getUrl());
        }
        return urlsList;
    }

    public void updateUrls(List<String> urls, long associationId, int type) {
        QueryWrapper<Image> wrapper = new QueryWrapper<>();
        wrapper.eq("association_id", associationId);
        wrapper.eq("type", type);
        this.remove(wrapper);
        List<Image> imageList = new ArrayList<>();
        for (String url : urls) {
            Image image = new Image();
            image.setType(1);
            image.setAssociationId(associationId);
            image.setUrl(url);
            image.setCreateTime(LocalDateTime.now());

            imageList.add(image);
        }
        this.saveBatch(imageList);
    }
}
