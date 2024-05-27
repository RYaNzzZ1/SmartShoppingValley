package com.smartshoppingvalley.Admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartshoppingvalley.Admin.entity.Admin;
import com.smartshoppingvalley.Admin.service.IAdminService;
import com.smartshoppingvalley.tools.JwtTokenProvider;
import com.smartshoppingvalley.tools.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/Admin/admin")
public class AdminController {
    @Autowired
    private IAdminService adminService;
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;

    @RequestMapping("/login")
    public Response adminLogin(@RequestBody Map<String, String> map) {
        log.info("调用adminLogin");
        String username = map.get("username");
        String password = map.get("password");

        int isLog = adminService.login(username, password);
        if (isLog == -1)
            return Response.error("账号或密码错误");
        if (isLog == -2)
            return Response.error("账号被禁用");
        if (isLog == 0)
            return Response.error("账号或密码错误");
        if (isLog == 1) {
            LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Admin::getUsername, username);
            Admin admin = adminService.getOne(wrapper);
            admin.setPassword(null);
            Long id = admin.getId();
            String token = JwtTokenProvider.generateToken(String.valueOf(id));
            Map<String, Object> res = new HashMap<>();
            log.info("生成token:  " + token);
            res.put("token", token);
            res.put("admin", admin);
            return Response.ok("登录成功").put("data", res);
        } else return Response.error("登录失败");
    }

    @RequestMapping("/getAdmins")
    public Response getAdmins() {
        log.info("调用getAdmins");
        List<Admin> res = adminService.getAll();
        for (Admin admin : res) {
            admin.setPassword(null);
        }
        return Response.ok("获取成功").put("data", res);
    }

    @RequestMapping("/changeAdminStatus")
    public Response changeAdminStatus(@RequestBody Map<String, Integer> map) {
        log.info("调用changeAdminStatus");
        int id = map.get("id");
        int res = adminService.changeAdminStatus(id);
        if (res == 1)
            return Response.ok("修改成功");
        return Response.error("修改失败");
    }

    @RequestMapping("/changeAdminInfo")
    public Response changeAdminInfo(@RequestBody Admin admin) {
        log.info("调用changeAdminInfo");
        int res = adminService.changeAdminInfo(admin);
        if (res == 1)
            return Response.ok("修改成功");
        return Response.error("修改失败");
    }

    @RequestMapping("/deleteAdmin")
    public Response deleteAdmin(@RequestBody Map<String, Integer> map) {
        log.info("调用deleteAdmin");
        int id = map.get("id");
        int res = adminService.deleteAdminById(id);
        if (res == 1)
            return Response.ok("删除成功");
        return Response.error("删除失败");
    }

    @RequestMapping("/addAdmin")
    public Response addAdmin(@RequestBody Admin admin) {
        log.info("调用addAdmin");
        int res = adminService.addAdmin(admin);
        if (res == 1)
            return Response.ok("添加成功");
        return Response.error("添加失败");
    }
}
