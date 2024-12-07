package com.shop.bookshop.controller.admin;

import com.shop.bookshop.domain.Dto.BookStockReport;
import com.shop.bookshop.services.OrderService;
import com.shop.bookshop.util.constant.FormatterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private FormatterUtil formatterUtil;

    @RequestMapping("/admin")
    public String admin() {
        return "admin/index";
    }



    @GetMapping("/admin/report")
    public String getReportPage(@RequestParam(required = false) LocalDate startDate,
                                @RequestParam(required = false) LocalDate endDate, Model model){

        if (startDate == null || endDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1);
            endDate = LocalDate.now();
        }

        // Lấy báo cáo doanh thu
        Map<LocalDate, Double> dailyRevenue = orderService.calculateRevenue(startDate, endDate);
        // Lấy báo cáo kho hàng
        List<BookStockReport> stockReport = orderService.getStockReport(startDate, endDate);

        model.addAttribute("stockReport", stockReport);
        model.addAttribute("dailyRevenue", dailyRevenue);
        model.addAttribute("formatter", formatterUtil);
        return "/admin/report";
    }


}
