package com.yummy.service.impl;

import com.github.pagehelper.util.StringUtil;
import com.yummy.dto.GoodsSalesDTO;
import com.yummy.entity.Orders;
import com.yummy.mapper.OrderDetailMapper;
import com.yummy.mapper.OrderMapper;
import com.yummy.mapper.UserMapper;
import com.yummy.service.ReportService;
import com.yummy.vo.OrderReportVO;
import com.yummy.vo.SalesTop10ReportVO;
import com.yummy.vo.TurnoverReportVO;
import com.yummy.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public TurnoverReportVO getTurnoverReport(LocalDate begin, LocalDate end) {
        //1.get date list
        List<LocalDate> dateList = getDateList(begin, end);

        //2.make turnover list
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.getAmountSumByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }

        //3.build VO
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    @Override
    public UserReportVO getUserReport(LocalDate begin, LocalDate end) {
        //get date list
        List<LocalDate> dateList = getDateList(begin, end);

        //get total and new user count list
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("begin", beginTime);
            Integer total = userMapper.getUserCount(map);
            map.put("end", endTime);
            Integer newTotal = userMapper.getUserCount(map);

            //get all user count: select count(*) from user where create_time < endTime
            //get user count created within [beginTime, endTime]?
            //select count(*) from user where create_time < endTime  and > beginTime
            totalUserList.add(total);
            newUserList.add(newTotal);
        }

        //build VO
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }

    @Override
    public OrderReportVO getOrderReport(LocalDate begin, LocalDate end) {
        //get date list
        List<LocalDate> dateList = getDateList(begin, end);

        //2.get order count list and valid order count list
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            //select count(*) from orders where create_time > beginTime and create_time < endTime
            //and status = completed
            Integer total = orderMapper.getOrderCountByMap(map);
            total = total == null ? 0 : total;
            orderCountList.add(total);
            map.put("status", Orders.COMPLETED);
            Integer validTotal = orderMapper.getOrderCountByMap(map);
            validTotal = validTotal == null ? 0 : validTotal;
            validOrderCountList.add(validTotal);
        }

        //3.get total order count and total valid order count
        Integer totalOrderCount  = 0;
        for (Integer orderCount : orderCountList) {
            totalOrderCount += orderCount;
        }
        Integer validOrderCount  = 0;
        for (Integer validCount : validOrderCountList) {
            validOrderCount += validCount;
        }

        //4.get order complete rate
        Double completeRate = 0.0;
        if(totalOrderCount != 0){
            completeRate = Double.valueOf(validOrderCount)/ Double.valueOf(totalOrderCount);
        }

        //5.build VO
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(completeRate)
                .build();
    }

    @Override
    public SalesTop10ReportVO getTop10SalesReport(LocalDate begin, LocalDate end) {
        //1.get begin time and end time
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        //2.get  name list
        //select name, count(*) from order_detail group by name order by count(*) desc
//        List<String> nameList = orderDetailMapper.getNameRank(beginTime, endTime);
//        List<String> top10NameList;
//        if(nameList.size() < 10){
//            top10NameList = nameList;
//            while(top10NameList.size() < 10){
//                top10NameList.add("NULL");
//            }
//        }else{
//            top10NameList = nameList.subList(0, 9);
//        }
//
//        //3.get number list
//        List<Integer> numberList = orderDetailMapper.getCountRank(beginTime, endTime);
//        List<Integer> top10NumberList;
//        if(numberList.size() < 10){
//            top10NumberList = numberList;
//            while(top10NumberList.size() < 10){
//                top10NumberList.add(0);
//            }
//        }else{
//            top10NumberList = numberList.subList(0, 9);
//        }

        //get name and number list from order_detail with order status completed
        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);
        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        String nameList = StringUtils.join(names, ",");

        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numberList = StringUtils.join(numbers, ",");

        return SalesTop10ReportVO.builder()
                .numberList(numberList)
                .nameList(nameList)
                .build();
    }

    private List<LocalDate> getDateList(LocalDate begin, LocalDate end){
        //get date list
        List<LocalDate> dateList = new ArrayList<>();

        while(!begin.equals(end)){
            dateList.add(begin);
            begin = begin.plusDays(1);
        }

        return dateList;
    }
}
