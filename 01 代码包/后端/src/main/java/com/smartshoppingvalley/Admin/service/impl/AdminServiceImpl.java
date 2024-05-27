package com.smartshoppingvalley.Admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartshoppingvalley.Admin.entity.Admin;
import com.smartshoppingvalley.Admin.mapper.AdminMapper;
import com.smartshoppingvalley.Admin.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Autowired
    private AdminMapper adminMapper;

    /**
     * @param username
     * @param password
     * @return -1 账号不存在
     * -2 账号被禁用
     * 0  密码错误
     * 1  登陆成功
     */
    public int login(String username, String password) {
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        Admin admin = adminMapper.selectOne(wrapper);
        if (admin == null)
            return -1;
        if (admin.getStatus() != 1)
            return -2;
        if (!admin.getPassword().equals(password))
            return 0;
        return 1;
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectList(null);
    }

    @Override
    public int changeAdminStatus(int id) {
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        Admin adminSearch = adminMapper.selectOne(wrapper);
        Admin admin = new Admin();
        if (adminSearch.getStatus() == 1)
            admin.setStatus(2);
        else admin.setStatus(1);
        return adminMapper.update(admin, wrapper);
    }

    @Override
    public int changeAdminInfo(Admin admin) {
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("id", admin.getId());
        return adminMapper.update(admin, wrapper);
    }

    @Override
    public int deleteAdminById(int id) {
        return adminMapper.deleteById(id);
    }

    @Override
    public int addAdmin(Admin admin) {
        return adminMapper.insert(admin);
    }


}
