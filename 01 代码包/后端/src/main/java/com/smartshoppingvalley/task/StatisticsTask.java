package com.smartshoppingvalley.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartshoppingvalley.Admin.entity.Statistics;
import com.smartshoppingvalley.Admin.service.IStatisticsService;
import com.smartshoppingvalley.Order.entity.OrderDetail;
import com.smartshoppingvalley.Order.entity.Orders;
import com.smartshoppingvalley.Order.service.IOrderDetailService;
import com.smartshoppingvalley.Order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class StatisticsTask {

    @Autowired
    private IStatisticsService statisticsService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderDetailService orderDetailService;

    @Scheduled(cron = "0 0 4 * * *") // 每天四点执行
    public void calculateStatistics() {
        LocalDate now = LocalDate.now();
        Statistics statistics = new Statistics();
        statistics.setCreateTime(now);

        //销售数据条数
        int count = statisticsService.count();

        //每天销售信息都要重新计算
        LambdaQueryWrapper<Orders> ordersWrapper = new LambdaQueryWrapper<>();
        ordersWrapper.eq(Orders::getStatus, 4);
        ordersWrapper.ge(Orders::getCreateTime, now.minusDays(1).atStartOfDay());
        ordersWrapper.le(Orders::getCreateTime, now.atStartOfDay());
        List<Orders> ordersList = orderService.list(ordersWrapper);

        //日销售额
        BigDecimal amount = BigDecimal.valueOf(0);

        List<Long> orderIds = new ArrayList<>();
        for (Orders orders : ordersList) {
            amount = amount.add(orders.getAmount());
            orderIds.add(orders.getId());
        }
        statistics.setDailyAmount(amount);

        //日销量
        Integer number = 0;
        List<OrderDetail> orderDetailList = orderDetailService.listByIds(orderIds);
        for (OrderDetail orderDetail : orderDetailList) {
            number += orderDetail.getNumber();
        }
        statistics.setDailyNum(number);

        //没有数据时
        if (count == 0) {
            statistics.setWeeklyNum(number);
            statistics.setWeeklyAmount(amount);
            statistics.setMonthlyNum(number);
            statistics.setMonthlyAmount(amount);
            statistics.setTotalNum(number);
            statistics.setTotalAmount(amount);

            statisticsService.save(statistics);
            return;
        }

        //获取前一天数据
        LambdaQueryWrapper<Statistics> statisticsWrapper = new LambdaQueryWrapper<>();
        statisticsWrapper.eq(Statistics::getCreateTime, now.minusDays(1));
        Statistics statisticsBeforeOneDay = statisticsService.getOne(statisticsWrapper);

        statistics.setTotalAmount(statisticsBeforeOneDay.getTotalAmount().add(amount));
        statistics.setTotalNum(statisticsBeforeOneDay.getTotalNum() + number);

        //条数小于7时，每周销售信息直接加当天销售信息
        if (count < 7) {
            statistics.setWeeklyAmount(statisticsBeforeOneDay.getWeeklyAmount().add(amount));
            statistics.setWeeklyNum(statisticsBeforeOneDay.getWeeklyNum() + number);
        }
        //条数大于等于7时，每周销售信息加当天销售信息，减一周前销售信息
        else {
            LambdaQueryWrapper<Statistics> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Statistics::getCreateTime, now.minusWeeks(1));
            Statistics statisticsBeforeOneWeek = statisticsService.getOne(wrapper);

            statistics.setWeeklyAmount(statisticsBeforeOneDay.getWeeklyAmount().add(amount).subtract(statisticsBeforeOneWeek.getDailyAmount()));
            statistics.setWeeklyNum(statisticsBeforeOneDay.getWeeklyNum() + number - statisticsBeforeOneWeek.getDailyNum());
        }

        //条数小于31时，每月销售信息直接加当天销售信息
        if (count < 31) {
            statistics.setMonthlyAmount(statisticsBeforeOneDay.getMonthlyAmount().add(amount));
            statistics.setMonthlyNum(statisticsBeforeOneDay.getMonthlyNum() + number);
        }
        //条数大于等于31时，每月销售信息加当天销售信息，减一月前销售信息
        else {
            LambdaQueryWrapper<Statistics> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Statistics::getCreateTime, now.minusMonths(1));
            Statistics statisticsBeforeOneMonth = statisticsService.getOne(wrapper);

            statistics.setMonthlyAmount(statisticsBeforeOneDay.getMonthlyAmount().add(amount).subtract(statisticsBeforeOneMonth.getDailyAmount()));
            statistics.setMonthlyNum(statisticsBeforeOneDay.getMonthlyNum() + number - statisticsBeforeOneMonth.getDailyNum());
        }

        statisticsService.save(statistics);
    }


}