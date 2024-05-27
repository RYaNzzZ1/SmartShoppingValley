package com.smartshoppingvalley.Admin.controller;


import com.smartshoppingvalley.Admin.entity.Shop;
import com.smartshoppingvalley.Admin.service.IShopService;
import com.smartshoppingvalley.tools.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/Admin/shop")
public class ShopController {
    @Autowired
    private IShopService shopService;

    @RequestMapping("/getShop")
    public Response getShop() {
        Shop shop = shopService.getShop();
        return Response.ok("获取成功").put("data", shop);
    }

    @RequestMapping("/changeShop")
    public Response changeShop(@RequestBody Shop shop) {
        int res = shopService.changeShop(shop);
        if (res == 1)
            return Response.ok("更新成功").put("data", shop);
        return Response.error("更新失败");
    }

}
