package com.yummy.controller.admin;

import com.yummy.result.Result;
import com.yummy.service.ReportService;
import com.yummy.vo.OrderReportVO;
import com.yummy.vo.SalesTop10ReportVO;
import com.yummy.vo.TurnoverReportVO;
import com.yummy.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Slf4j
@Api(tags = "Report APIs")
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/turnoverStatistics")
    @ApiOperation("Turnover report")
    public Result<TurnoverReportVO> turnoverReport(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("get turnover report within: {}, {}", begin, end);
        TurnoverReportVO turnoverReportVO = reportService.getTurnoverReport(begin, end);
        return Result.success(turnoverReportVO);
    }

    @GetMapping("/userStatistics")
    @ApiOperation("User report")
    public Result<UserReportVO> userReport(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                           @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("get user report within :{}, {}", begin, end);
        UserReportVO userReportVO =  reportService.getUserReport(begin, end);
        return Result.success(userReportVO);
    }

    @GetMapping("/ordersStatistics")
    @ApiOperation("Order report")
    public Result<OrderReportVO> orderReport(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("get order report within :{}, {}", begin, end);
        OrderReportVO orderReportVO =  reportService.getOrderReport(begin, end);
        return Result.success(orderReportVO);
    }

    @GetMapping("/top10")
    @ApiOperation("Top10 dish report")
    public Result<SalesTop10ReportVO> top10Report(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("get top10 sales within :{}, {}", begin, end);
        SalesTop10ReportVO salesTop10ReportVO =  reportService.getTop10SalesReport(begin, end);
        return Result.success(salesTop10ReportVO);
    }
}
