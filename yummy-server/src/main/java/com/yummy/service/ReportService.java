package com.yummy.service;

import com.yummy.vo.OrderReportVO;
import com.yummy.vo.SalesTop10ReportVO;
import com.yummy.vo.TurnoverReportVO;
import com.yummy.vo.UserReportVO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@Service
public interface ReportService {
    TurnoverReportVO getTurnoverReport(LocalDate begin, LocalDate end);

    UserReportVO getUserReport(LocalDate begin, LocalDate end);

    OrderReportVO getOrderReport(LocalDate begin, LocalDate end);

    SalesTop10ReportVO getTop10SalesReport(LocalDate begin, LocalDate end);

    void exportBusinessData(HttpServletResponse response);
}


