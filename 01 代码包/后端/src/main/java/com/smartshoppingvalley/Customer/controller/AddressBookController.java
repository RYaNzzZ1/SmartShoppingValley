package com.smartshoppingvalley.Customer.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.smartshoppingvalley.Customer.entity.AddressBook;
import com.smartshoppingvalley.Customer.service.IAddressBookService;
import com.smartshoppingvalley.tools.GetToken;
import com.smartshoppingvalley.tools.Response;
import com.smartshoppingvalley.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 地址管理 前端控制器
 * </p>
 */
@RestController
@Slf4j
@RequestMapping("/Customer/address-book")
public class AddressBookController {
    @Autowired
    private IAddressBookService addressBookService;

    @Autowired
    private HttpServletRequest request;

    //用户获取全部收货地址
    @RequestMapping("/getAll")
    public Response getAddresses(HttpServletRequest request) {
        log.info("获取用户收货地址");

        //获取用户id
        String id = GetToken.getIdFromToken(GetToken.getTokenFromRequest(request));

        //获取收货地址列表
        List<AddressBook> res = addressBookService.getAddressesById(Integer.parseInt(id));
        return Response.ok("获取成功").put("data", res);
    }

    //用户添加收货地址
    @RequestMapping("/add")
    public Response addAddress(@RequestBody AddressBook addr) {
        log.info("添加收货地址");

        //获取用户id
        String token = GetToken.getTokenFromRequest(request);
        String id = GetToken.getIdFromToken(token);
        addr.setCustomerId(Long.valueOf(id));

        int res = addressBookService.addAddr(addr);
        if (res == 1)
            return Response.ok("添加成功");
        return Response.error("添加失败");
    }

    //用户删除收货地址
    @RequestMapping("/delete")
    public Response deleteAddress(@RequestBody Map<String, Integer> map) {
        log.info("删除收货地址");

        int id = map.get("addrId");
        int res = addressBookService.deleteAddrById(id);
        if (res == 1)
            return Response.ok("删除成功");
        return Response.error("删除失败");
    }

    //用户修改收货地址
    @RequestMapping("/edit")
    public Response editAddress(@RequestBody AddressBook addr) {
        log.info("修改收货地址");

        int res = addressBookService.editAddress(addr);
        if (res == 1)
            return Response.ok("修改成功");
        return Response.error("修改失败");
    }

    //设置默认收获地址
    @RequestMapping("/setDefault")
    @Transactional
    public Response defaultAddress(@RequestBody Map<String, Integer> map) {
        log.info("设置默认收货地址");

        //获取用户id
        String token = GetToken.getTokenFromRequest(request);
        String idFromToken = GetToken.getIdFromToken(token);
        Long customerId = Long.valueOf(idFromToken);

        //先将其他收货地址设为非默认
        LambdaUpdateWrapper<AddressBook> addressBookLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        addressBookLambdaUpdateWrapper.eq(AddressBook::getCustomerId, customerId);
        addressBookLambdaUpdateWrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(addressBookLambdaUpdateWrapper);

        //将该地址设为默认
        Long id = Long.valueOf(map.get("id"));
        AddressBook addressBook = new AddressBook();
        addressBook.setId(id);
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);

        return Response.ok("设置成功");
    }

    //获取默认地址
    @RequestMapping("/getDefault")
    public Result getDefault() {
        log.info("获取默认地址");

        //获取用户id
        String token = GetToken.getTokenFromRequest(request);
        String idFromToken = GetToken.getIdFromToken(token);
        Long customerId = Long.valueOf(idFromToken);

        //获取默认地址
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getCustomerId, customerId);
        wrapper.eq(AddressBook::getIsDefault, 1);
        wrapper.eq(AddressBook::getIsDeleted, 0);
        AddressBook one = addressBookService.getOne(wrapper);
        return Result.success(one);
    }
}
