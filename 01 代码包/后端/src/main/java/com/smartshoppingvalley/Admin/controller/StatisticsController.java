package com.smartshoppingvalley.Admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartshoppingvalley.Admin.dto.StatisticsDto;
import com.smartshoppingvalley.Admin.entity.Statistics;
import com.smartshoppingvalley.Admin.service.IStatisticsService;
import com.smartshoppingvalley.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/Admin/statistics")
public class StatisticsController {

    private static LocalDate localDate = LocalDate.now().minusMonths(1);
    @Autowired
    private IStatisticsService statisticsService;

    //添加数据（用于数据库插入数据）
    @PostMapping("/add")
    public Result add() {
        log.info("添加数据（用于数据库插入数据）");
        LocalDate now = LocalDate.now();
        Integer num = 1;
        BigDecimal amount = BigDecimal.valueOf(1);

        while (localDate.isBefore(now.plusDays(1))) {
            Statistics statistics = new Statistics();
            statistics.setDailyNum(num);
            statistics.setMonthlyNum(num);
            statistics.setWeeklyNum(num);
            statistics.setTotalNum(num);

            statistics.setDailyAmount(amount);
            statistics.setMonthlyAmount(amount);
            statistics.setWeeklyAmount(amount);
            statistics.setTotalAmount(amount);

            statistics.setCreateTime(localDate);

            statisticsService.save(statistics);
            num++;
            amount = amount.add(BigDecimal.valueOf(1));
            localDate = localDate.plusDays(1);
        }

        return Result.success();
    }

    //获取销售数据
    @GetMapping("/getStatistics")
    public Result getStatistics() {
        log.info("获取销售数据");

        //返回数据
        StatisticsDto statisticsDto = new StatisticsDto();

        LocalDate now = LocalDate.now();
        LambdaQueryWrapper<Statistics> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Statistics::getCreateTime, now);
        Statistics statistics = statisticsService.getOne(wrapper);
        BeanUtils.copyProperties(statistics, statisticsDto);


        wrapper = new LambdaQueryWrapper<>();
        wrapper.gt(Statistics::getCreateTime, now.minusWeeks(1));
        wrapper.le(Statistics::getCreateTime, now);
        wrapper.orderByAsc(Statistics::getCreateTime);
        List<Statistics> statisticsList = statisticsService.list(wrapper);

        //一周的每日销量
        List<Integer> nums = new ArrayList<>();

        //一周的每日销售额
        List<BigDecimal> amounts = new ArrayList<>();

        for (Statistics s : statisticsList) {
            nums.add(s.getDailyNum());
            amounts.add(s.getDailyAmount());
        }
        statisticsDto.setNums(nums);
        statisticsDto.setAmounts(amounts);

        return Result.success(statisticsDto);
    }

}
