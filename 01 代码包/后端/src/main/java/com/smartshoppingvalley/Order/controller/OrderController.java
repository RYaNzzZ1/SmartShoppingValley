package com.smartshoppingvalley.Order.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartshoppingvalley.Commodity.entity.Commodity;
import com.smartshoppingvalley.Commodity.service.ICommodityService;
import com.smartshoppingvalley.Customer.entity.ShoppingCart;
import com.smartshoppingvalley.Customer.service.IShoppingCartService;
import com.smartshoppingvalley.Discount.service.ICouponsDetailService;
import com.smartshoppingvalley.Order.dto.OrderDto;
import com.smartshoppingvalley.Order.entity.OrderDetail;
import com.smartshoppingvalley.Order.entity.Orders;
import com.smartshoppingvalley.Order.service.IOrderDetailService;
import com.smartshoppingvalley.Order.service.IOrderService;
import com.smartshoppingvalley.tools.GetToken;
import com.smartshoppingvalley.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 */
@RestController
@Slf4j

@RequestMapping("/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @Autowired
    private IShoppingCartService shoppingCartService;

    @Autowired
    private IOrderDetailService orderDetailService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ICouponsDetailService couponsDetailService;

    @Autowired
    private ICommodityService commodityService;


    //用户提交订单
    @PostMapping("/submit")
    @Transactional
    public Result submit(@RequestBody OrderDto orderDto) {
        log.info("用户提交订单");

        //TODO 获取用户id
//        Long customerId = 1L;
        String token = GetToken.getTokenFromRequest(request);
        String id = GetToken.getIdFromToken(token);
        Long customerId = Long.valueOf(id);

        LocalDateTime now = LocalDateTime.now();
        //写入订单表
        orderDto.setCustomerId(customerId);
        orderDto.setOrderTime(now);
        orderDto.setCheckoutTime(now);
        orderDto.setStatus(2);
        orderDto.setCreateTime(now);

        orderService.save(orderDto);

        //把购物车数据写入订单
        Long ordersId = orderDto.getId();
        List<OrderDetail> orderDetailList = new ArrayList<>();

        List<ShoppingCart> shoppingCartList = shoppingCartService.listByCustomerId(customerId);

        for (ShoppingCart shoppingCart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(ordersId);
            //购买量
            Integer number = shoppingCart.getNumber();
            orderDetail.setNumber(number);
            orderDetail.setAmount(shoppingCart.getAmount());
            orderDetail.setCommodityAttribute(shoppingCart.getCommodityAttribute());
            //商品id
            Long commodityId = shoppingCart.getCommodityId();
            orderDetail.setCommodityId(commodityId);

            //更改库存量
            Commodity commodity = commodityService.getById(commodityId);
            commodity.setNum(commodity.getNum() - number);
            commodityService.updateById(commodity);

            orderDetailList.add(orderDetail);
        }
        orderDetailService.saveBatch(orderDetailList);

        //清空购物车
        shoppingCartService.clearByCustomerId(customerId);

        List<Long> couponsIds = orderDto.getCouponsIds();
        //标记优惠券未已使用
        couponsDetailService.use(customerId, couponsIds);

        return Result.success();
    }


//    //用户订单付款
//    @PutMapping("/pay")
//    public Result pay(@RequestBody Orders orders) {
//        log.info("用户订单付款");
//
//        orders.setStatus(2);
//        orders.setCheckoutTime(LocalDateTime.now());
//
//        orderService.updateById(orders);
//        return Result.success();
//    }

//    //用户取消订单
//    @PutMapping("/cancel")
//    public Result cancel(@RequestBody Orders orders) {
//        log.info("用户取消订单");
//
//        orders.setStatus(5);
//        orderService.updateById(orders);
//
//        return Result.success();
//    }

    //用户订单完成
    @PutMapping("/complete")
    public Result complete(@RequestBody Orders orders) {
        log.info("用户订单完成");

        orders.setStatus(4);
        orderService.updateById(orders);

        return Result.success();
    }

    //用户删除订单（逻辑删，is_deleted设为1）
    @DeleteMapping("/delete")
    public Result delete(@RequestBody Orders orders) {
        log.info("用户删除订单（逻辑删，is_deleted设为1）");

        orders.setIsDeleted(1);
        orderService.updateById(orders);

        return Result.success();
    }

    //用户查看订单列表
    @GetMapping("/list")
    public Result list() {
        log.info("用户查看订单列表");

        //TODO 获取用户id
//        Long customerId = 1L;
        String token = GetToken.getTokenFromRequest(request);
        String id = GetToken.getIdFromToken(token);
        Long customerId = Long.valueOf(id);

        //根据用户id查询订单列表
        List<Orders> ordersList = orderService.listByCustomerId(customerId);

        return Result.success(ordersList);
    }


    //加入购物车（复购）
    @PostMapping("/repurchase")
    public Result repurchase(@RequestBody Orders orders) {
        log.info("加入购物车（复购）");

        //TODO 获取用户id
//        Long customerId = 1L;
        String token = GetToken.getTokenFromRequest(request);
        String id = GetToken.getIdFromToken(token);
        Long customerId = Long.valueOf(id);

        Long ordersId = orders.getId();
        List<OrderDetail> orderDetailList = orderDetailService.listByOrderId(ordersId);

        //要放入购物车的数据
        List<ShoppingCart> shoppingCartList = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        for (OrderDetail orderDetail : orderDetailList) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setCustomerId(customerId);
            shoppingCart.setCommodityId(orderDetail.getCommodityId());
            shoppingCart.setCommodityAttribute(orderDetail.getCommodityAttribute());
            shoppingCart.setNumber(orderDetail.getNumber());
            shoppingCart.setAmount(orderDetail.getAmount());
            shoppingCart.setCreateTime(now);

            shoppingCartList.add(shoppingCart);
        }
        shoppingCartService.saveBatch(shoppingCartList);

        return Result.success();
    }


    //管理员查看订单列表（分页）
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, Integer> map) {
        log.info("管理员查看订单列表（分页）");

        Integer page = map.get("page");
        Integer limit = map.get("limit");

        Page<Orders> ordersPage = new Page<>(page, limit);
        orderService.page(ordersPage);
        return Result.success(ordersPage);
    }


    //管理员设置订单派送
    @PutMapping("/deliver")
    public Result deliver(@RequestBody Orders orders) {
        log.info("管理员设置订单派送");

        orders.setStatus(3);
        orderService.updateById(orders);

        return Result.success();
    }

    /*@RequestMapping("create")
    public Result add_order(@RequestBody Orders order) {
        order.setOrderTime(LocalDateTime.now());
        order.setCreateTime(LocalDateTime.now());
        boolean result = iOrderService.save(order);
        if (result)
            return Result.success();
        else
            return Result.error("添加失败");
    }*/

    /*@RequestMapping("/show")
    public Result show_order(@RequestParam long id) {
        Orders order = iOrderService.getById(id);
        if (order != null)
            return Result.success(order);
        else
            return Result.error("找不到该订单");
    }*/

    /*@RequestMapping("/update")
    public Result update_order(@RequestParam long id, @RequestParam(required = false) Integer status, @RequestParam(required = false) Integer payMethod) {
        Orders order = iOrderService.getById(id);

        if (null != status)
            order.setStatus(status);
        if (null != payMethod)
            order.setPayMethod(payMethod);
        iOrderService.updateById(order);
        if (order != null && (status != null || payMethod != null))
            return Result.success();
        else
            return Result.error("更新失败");
    }*/
}
