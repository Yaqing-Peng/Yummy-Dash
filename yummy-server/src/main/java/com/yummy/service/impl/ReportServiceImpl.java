package com.yummy.service.impl;

import com.github.pagehelper.util.StringUtil;
import com.yummy.dto.GoodsSalesDTO;
import com.yummy.entity.Orders;
import com.yummy.mapper.OrderDetailMapper;
import com.yummy.mapper.OrderMapper;
import com.yummy.mapper.UserMapper;
import com.yummy.service.ReportService;
import com.yummy.service.WorkspaceService;
import com.yummy.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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
    private WorkspaceService workspaceService;

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

    @Override
    public void exportBusinessData(HttpServletResponse response) {
        //1.get data from sql - recent 30 days
        LocalDate beginDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().minusDays(1);

        BusinessDataVO businessData = workspaceService.getBusinessData(
                LocalDateTime.of(beginDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX));

        //2.write data into excel by POI
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/business-data-template.xlsx");
        try {
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);

            //fill overview data
            XSSFSheet sheet1 = excel.getSheet("Sheet1");
            sheet1.getRow(1).getCell(1).setCellValue("Period: from " + beginDate + " to " + endDate);

            XSSFRow row = sheet1.getRow(3);
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());

            row = sheet1.getRow(4);
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());

            //fill order details
            for (int i = 0; i < 30; i++) {
                LocalDate date = beginDate.plusDays(i);
                BusinessDataVO businessData1 = workspaceService.getBusinessData(
                        LocalDateTime.of(date, LocalTime.MIN),
                        LocalDateTime.of(date, LocalTime.MAX)
                );
                row = sheet1.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData1.getTurnover());
                row.getCell(3).setCellValue(businessData1.getValidOrderCount());
                row.getCell(4).setCellValue(businessData1.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData1.getUnitPrice());
                row.getCell(6).setCellValue(businessData1.getNewUsers());
            }

            //3.download excel into client
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

            out.close();
            excel.close();

        }catch (IOException e){
            e.printStackTrace();
        }


    }

}
