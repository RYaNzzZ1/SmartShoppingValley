package com.smartshoppingvalley.Customer.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smartshoppingvalley.Customer.entity.Customer;
import com.smartshoppingvalley.Customer.service.ICustomerService;
import com.smartshoppingvalley.tools.GetToken;
import com.smartshoppingvalley.tools.JwtTokenProvider;
import com.smartshoppingvalley.tools.RandomNumberGenerator;
import com.smartshoppingvalley.tools.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wyh
 * @since 2023-08-28
 */
@RestController
//@CrossOrigin
@Slf4j
@RequestMapping("/Customer/customer")
public class CustomerController {
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    //发送验证码
    @RequestMapping("/sendCode")
    public Response sendCode(@RequestBody Map<String, String> map) {
        log.info("发送验证码");

        String username = map.get("username");
        int verificationCode = RandomNumberGenerator.generate();
        customerService.sendVerificationCode(username, verificationCode);

        //根据验证码生成codeToken发送给前端，用于后续验证
        String codeToken = JwtTokenProvider.generateToken(Integer.toString(verificationCode));
        log.info("发送codeToken：  " + codeToken);
        return Response.ok("发送成功").put("codeToken", codeToken);
    }

    //新用户注册
    @RequestMapping("/register")
    public Response register(@RequestBody Map<String, String> map) {
        log.info("用户注册");

        String username = map.get("username");
        String password = map.get("password");
        String verificationCode = map.get("verificationCode");
        String codeToken = map.get("codeToken");

        //验证码是否过期
        if (!JwtTokenProvider.validateToken(codeToken))
            return Response.error("验证码已过期");

        //判断该用户是否已注册
        Long isExist = customerService.getIdByUsername(username);
        if (isExist != -1)
            return Response.error("用户已存在");

        //验证码是否正确
        log.info("接收验证码token：  " + codeToken);
        String codeFromToken = GetToken.getIdFromToken(codeToken);
        if (!verificationCode.equals(codeFromToken)) {
            log.info("验证码错误");
            return Response.error("验证码错误");
        }

        //注册用户
        Customer customer = new Customer(username, password);
        int res = customerService.saveCustomer(customer);
        if (res == 1) {
            return Response.ok("注册成功");
        } else {
            return Response.error("注册失败");
        }
    }

    //密码登录
    @RequestMapping("/loginByPW")
    public Response logInByPW(@RequestBody Map<String, String> map) {
        log.info("密码登录");

        String username = map.get("username");
        String password = map.get("password");
        Long id = customerService.getIdByUsername(username);
        if (id == -1)
            return Response.error("用户不存在");
        int is = customerService.isPass(username, password);
        if (is == -1)
            return Response.error("用户被禁用");
        if (is == -2)
            return Response.error("密码错误");

        //登陆成功后返回token
        String token = JwtTokenProvider.generateToken(id.toString());
        Map<String, String> res = new HashMap<>();
        res.put("token", token);
        return Response.ok("登录成功").put("data", res);

    }

    //验证码登录
    @RequestMapping("/loginByCode")
    public Response logInByCode(@RequestBody Map<String, String> map) {
        log.info("验证码登录");

        String username = map.get("username");
        String verificationCode = map.get("verificationCode");
        String codeToken = map.get("codeToken");

        //验证码是否过期
        if (!JwtTokenProvider.validateToken(codeToken))
            return Response.error("验证码已过期");

        //首先判断用户是否存在
        Long id = customerService.getIdByUsername(username);
        if (id == -1)
            return Response.error("用户不存在");

        //验证验证码
        String codeFromToken = GetToken.getIdFromToken(codeToken);
        if (!verificationCode.equals(codeFromToken))
            return Response.error("验证码错误");

        String token = JwtTokenProvider.generateToken(id.toString());
        Map<String, String> res = new HashMap<>();
        res.put("token", token);
        return Response.ok("登录成功").put("data", res);

    }

    //修改密码
    @RequestMapping("/editPassword")
    public Response editPassword(@RequestBody Map<String, String> map, HttpServletRequest request) {
        String password = map.get("password");
        String codeToken = map.get("codeToken");
        String verificationCode = map.get("verificationCode");

        //判断验证码是否正确
        String codeFromToken = GetToken.getIdFromToken(codeToken);
        if (!codeFromToken.equals(verificationCode))
            return Response.error("验证码错误");

        //修改密码
        Long id = Long.valueOf(GetToken.getIdFromToken(GetToken.getTokenFromRequest(request)));
        LambdaUpdateWrapper<Customer> wrapper = new LambdaUpdateWrapper();
        wrapper.eq(Customer::getId, id);
        wrapper.set(Customer::getPassword, password);
        customerService.update(wrapper);

        return Response.ok("修改成功");

    }


    //获取用户列表
    @RequestMapping("/getUsers")
    public Response getUsersAtPage(@RequestBody Map<String, Integer> map) {
        log.info("获取用户列表");

        int page = map.get("page");
        int size = map.get("limit");
        IPage<Customer> customerIPage = customerService.getCustomerAtPage(page, size);

        return Response.ok("获取成功").put("data", customerIPage);
    }

    //搜索用户
    @RequestMapping("/searchUsers")
    public Response searchUsersAtPage(@RequestBody Map<String, String> map) {
        log.info("搜索用户");

        int page = Integer.parseInt(map.get("page"));
        int size = Integer.parseInt(map.get("limit"));
        String name = map.get("username");
        IPage<Customer> customerIPage = customerService.searchCustomerAtPage(page, size, name);
        return Response.ok("获取成功").put("data", customerIPage);
    }

    //修改用户状态
    @RequestMapping("/changeStatus")
    public Response changeCustomerStatus(@RequestBody Map<String, Integer> map) {
        log.info("修改用户状态");
        int id = map.get("id");
        int res = customerService.changeCustomerStatus(id);
        if (res == 1) {
            return Response.ok("修改成功");
        } else return Response.error("修改失败");
    }

}


