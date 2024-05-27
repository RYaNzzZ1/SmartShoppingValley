package com.smartshoppingvalley.ImageFile.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartshoppingvalley.ImageFile.entity.Image;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 */
public interface IImageService extends IService<Image> {
    List<String> getUrls(long associationId, int type);

    void updateUrls(List<String> urls, long associationId, int type);
}
