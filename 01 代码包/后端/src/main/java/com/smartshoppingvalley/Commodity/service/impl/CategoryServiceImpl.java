package com.smartshoppingvalley.Commodity.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartshoppingvalley.Commodity.entity.Category;
import com.smartshoppingvalley.Commodity.mapper.CategoryMapper;
import com.smartshoppingvalley.Commodity.service.ICategoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜品及套餐分类 服务实现类
 * </p>
 *
 * @author wyh
 * @since 2023-09-01
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

   /* @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ICommodityService commodityService;
    @Override
    public IPage<Category> getCategoryListByFid(int p, int size, int fid) {
        QueryWrapper wrapper=new QueryWrapper<>();
        wrapper.eq("father_id",fid);
        Page<Category> page=new Page<>(p,size);
        IPage<Category> categoryIPage=categoryMapper.selectPage(page,wrapper);
        return categoryIPage;
    }

    @Override
    public int addType(int fid, String name) {
        Category category=new Category((long) fid,name);
        return categoryMapper.insert(category);
    }

    @Override
    public int editType(Category category) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("id",category.getId());
        return categoryMapper.update(category,wrapper);
    }*/
}
