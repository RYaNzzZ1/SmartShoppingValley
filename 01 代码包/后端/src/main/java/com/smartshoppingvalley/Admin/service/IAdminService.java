package com.smartshoppingvalley.Admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartshoppingvalley.Admin.entity.Admin;

import java.util.List;


/**
 * <p>
 * 服务类
 * </p>
 */
public interface IAdminService extends IService<Admin> {
    /**
     * 登录验证
     */
    int login(String username, String password);

    /**
     * 获取所有管理员列表
     */
    List<Admin> getAll();

    /**
     * 修改管理员状态
     */
    int changeAdminStatus(int id);

    /**
     * 修改管理员信息
     */
    int changeAdminInfo(Admin admin);


    int deleteAdminById(int id);

    int addAdmin(Admin admin);
}
