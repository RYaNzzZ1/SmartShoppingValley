package com.smartshoppingvalley.Order.controller;


import com.smartshoppingvalley.Comments.service.ICommentsService;
import com.smartshoppingvalley.Commodity.entity.Commodity;
import com.smartshoppingvalley.Commodity.service.ICommodityService;
import com.smartshoppingvalley.Customer.entity.AddressBook;
import com.smartshoppingvalley.Customer.service.IAddressBookService;
import com.smartshoppingvalley.Discount.service.ICouponsDetailService;
import com.smartshoppingvalley.ImageFile.service.IImageService;
import com.smartshoppingvalley.Order.dto.CommodityInOrders;
import com.smartshoppingvalley.Order.dto.OrderDetailDto;
import com.smartshoppingvalley.Order.dto.OrderDto;
import com.smartshoppingvalley.Order.entity.OrderDetail;
import com.smartshoppingvalley.Order.entity.Orders;
import com.smartshoppingvalley.Order.service.IOrderDetailService;
import com.smartshoppingvalley.Order.service.IOrderService;
import com.smartshoppingvalley.tools.GetToken;
import com.smartshoppingvalley.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单明细表 前端控制器
 * </p>
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/order/detail")
public class OrderDetailController {
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IAddressBookService addressBookService;
    @Autowired
    private ICommodityService commodityService;
    @Autowired
    private IImageService imageService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ICommentsService commentsService;

    @Autowired
    private ICouponsDetailService couponsDetailService;


    //立即购买商品
    @PostMapping("/purchase")
    @Transactional
    public Result purchase(@RequestBody OrderDetailDto orderDetailDto) {
        log.info("立即购买商品");

        //当前登录用户
        String id = GetToken.getIdFromToken(GetToken.getTokenFromRequest(request));
        Long customerId = Long.valueOf(id);
        LocalDateTime now = LocalDateTime.now();

        //保存订单
        Orders orders = new Orders();
        orders.setCustomerId(customerId);
        orders.setStatus(2);
        orders.setOrderTime(now);
        orders.setCheckoutTime(now);
        orders.setCreateTime(now);
        orders.setAddressBookId(orderDetailDto.getAddressBookId());
        orders.setPayMethod(orderDetailDto.getPayMethod());
        orders.setAmount(orderDetailDto.getAmount());
        orders.setRemark(orderDetailDto.getRemark());
        orderService.save(orders);

        //订单id
        Long ordersId = orders.getId();
        //保存订单明细
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(ordersId);
        //商品id
        Long commodityId = orderDetailDto.getCommodityId();
        orderDetail.setCommodityId(commodityId);
        orderDetail.setCommodityAttribute(orderDetailDto.getCommodityAttribute());
        //购买量
        Integer number = orderDetailDto.getNumber();
        orderDetail.setNumber(number);
        orderDetail.setAmount(orderDetailDto.getAmount());

        //更改库存量
        Commodity commodity = commodityService.getById(commodityId);
        commodity.setNum(commodity.getNum() - number);
        commodityService.updateById(commodity);

        List<Long> couponsIds = orderDetailDto.getCouponsIds();
        //标记优惠券未已使用
        couponsDetailService.use(customerId, couponsIds);


        orderDetailService.save(orderDetail);
        return Result.success();
    }


    //查看订单明细
    @PostMapping("/show")
    public Result show(@RequestBody Orders orders) {
        log.info("查看订单明细");

        //返回数据
        OrderDto orderDto = new OrderDto();

        //获取用户id
        String token = GetToken.getTokenFromRequest(request);
        String id = GetToken.getIdFromToken(token);
        Long customerId = Long.valueOf(id);

        //根据id查订单
        Long ordersId = orders.getId();
        orders = orderService.getById(ordersId);
        BeanUtils.copyProperties(orders, orderDto);

        //获取地址簿
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        orderDto.setAddressBook(addressBook);

        //查询订单商品
        List<CommodityInOrders> commodityInOrdersList = new ArrayList<>();

        List<OrderDetail> orderDetailList = orderDetailService.listByOrderId(ordersId);
        for (OrderDetail orderDetail : orderDetailList) {
            Commodity commodity = commodityService.getById(orderDetail.getCommodityId());
            CommodityInOrders commodityInOrders = CommodityInOrders.fromCommodity(commodity);
            commodityInOrders.setCommodityAttribute(orderDetail.getCommodityAttribute());
            commodityInOrders.setNumber(orderDetail.getNumber());
            commodityInOrders.setAmount(orderDetail.getAmount());

            if (commentsService.isCommented(customerId, ordersId, commodity.getId()))
                commodityInOrders.setIsCommented(1);
            else commodityInOrders.setIsCommented(0);

            commodityInOrders.setUrls(imageService.getUrls(commodity.getId(), 1));

            commodityInOrdersList.add(commodityInOrders);
        }

        orderDto.setCommodityInOrdersList(commodityInOrdersList);

        return Result.success(orderDto);
    }

//    @RequestMapping("/add")
//    public Result add_order(@RequestBody OrderDetail orderDetail)
//    {
//        boolean result =iOrderDetailService.save(orderDetail);
//        if(result)
//            return Result.success();
//        else
//            return Result.error("添加失败");
//    }
//    @RequestMapping("/show")
//    public Result show_order(@RequestParam long id) {
//        OrderDetail orderDetail = iOrderDetailService.getById(id);
//        long order_id=orderDetail.getOrderId();
//        Orders order = iOrderService.getById(order_id);
//        if (orderDetail != null && order != null) {
//            Map<String, Object> result = new HashMap<>();
//            result.put("order", order);
//            result.put("orderDetail", orderDetail);
//            return Result.success(result);
//        } else {
//            return Result.error("找不到该订单详情");
//        }
//    }

}
