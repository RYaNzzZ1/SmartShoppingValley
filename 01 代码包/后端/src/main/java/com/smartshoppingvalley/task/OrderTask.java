//package com.smartshoppingvalley.task;
//
//import com.smartshoppingvalley.Order.entity.Orders;
//import com.smartshoppingvalley.Order.service.IOrderService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class OrderTask {
//
//    @Autowired
//    private IOrderService orderService;
//
//    @Scheduled(initialDelay = 60 * 1000, fixedDelay = 60 * 1000) // 每分钟执行一次
//    @Transactional
//    public void cancelOrders() {
//        //获取未付款订单
//        List<Orders> unpaidOrders = orderService.getUnpaidOrders();
//        //超时订单
//        List<Orders> timeoutOrders = new ArrayList<>();
//
//        for (Orders orders : unpaidOrders) {
//            LocalDateTime orderTime = orders.getOrderTime();
//            LocalDateTime now = LocalDateTime.now();
//            Duration duration = Duration.between(orderTime, now);
//            if (duration.toMinutes() >= 10) {
//                Orders o = new Orders();
//                o.setId(orders.getId());
//                o.setStatus(5);
//                timeoutOrders.add(o);
//            }
//        }
//
//        if (!timeoutOrders.isEmpty())
//            orderService.updateBatchById(timeoutOrders);
//
//    }
//}
