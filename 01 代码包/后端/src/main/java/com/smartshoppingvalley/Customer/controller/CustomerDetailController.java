package com.smartshoppingvalley.Customer.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartshoppingvalley.Customer.entity.CustomerDetail;
import com.smartshoppingvalley.Customer.service.ICustomerDetailService;
import com.smartshoppingvalley.tools.GetToken;
import com.smartshoppingvalley.tools.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 */
@RestController
@Slf4j
//@CrossOrigin
@RequestMapping("/Customer/customer-detail")
public class CustomerDetailController {

    @Autowired
    private ICustomerDetailService customerDetailService;

    //管理员端
    @RequestMapping("/getUserDetail")
    public Response getCustomerDetail(@RequestBody Map<String, Integer> map) {
        log.info("获取用户详情");

        int id = map.get("id");
        CustomerDetail cus = customerDetailService.getCustomerDetail(id);
        if (cus != null)
            return Response.ok("获取成功").put("data", cus);
        else
            return Response.ok("用户暂未添加详情").put("data", null);
    }

    //用户端
    @RequestMapping("/getCustomerData")
    public Response getCustomerData(HttpServletRequest request) {
        log.info("获取用户详情");

        int id = Integer.parseInt(GetToken.getIdFromToken(GetToken.getTokenFromRequest(request)));
        CustomerDetail cus = customerDetailService.getCustomerDetail(id);
        if (cus != null)
            return Response.ok("获取成功").put("data", cus);
        else
            return Response.ok("用户暂未添加详情").put("data", null);
    }

    //管理员端
    @RequestMapping("/edit")
    public Response editUserDetail(@RequestBody CustomerDetail customerDetail) {
        log.info("修改用户详细信息");
        //先查表中是否有该用户详细信息，有则修改，无则添加
        LambdaQueryWrapper<CustomerDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerDetail::getCustomerId, customerDetail.getCustomerId());
        CustomerDetail cus = customerDetailService.getOne(wrapper);

        if (cus == null) {
            customerDetailService.addCustomerDetail(customerDetail);
            return Response.ok("添加成功");
        } else {
            customerDetailService.update(customerDetail, wrapper);
            return Response.ok("修改成功");
        }
    }

    //用户端
    @RequestMapping("/editCustomer")
    public Response editCustomer(@RequestBody CustomerDetail customerDetail, HttpServletRequest request) {
        log.info("修改用户详细信息");

        //从token获取id，set到customerDetail
        Long id = Long.valueOf(GetToken.getIdFromToken(GetToken.getTokenFromRequest(request)));
        customerDetail.setCustomerId(id);

        //先查表中是否有该用户详细信息，有则修改，无则添加
        LambdaQueryWrapper<CustomerDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerDetail::getCustomerId, customerDetail.getCustomerId());
        CustomerDetail cus = customerDetailService.getOne(wrapper);

        if (cus == null) {
            customerDetailService.addCustomerDetail(customerDetail);
            return Response.ok("添加成功");
        } else {
            customerDetailService.update(customerDetail, wrapper);
            return Response.ok("修改成功");
        }
    }
}
