package com.shop.bookshop.controller.client;

import com.shop.bookshop.domain.Dto.PromotionDTO;
import com.shop.bookshop.domain.Order;
import com.shop.bookshop.domain.Promotion;
import com.shop.bookshop.domain.User;
import com.shop.bookshop.services.BookService;
import com.shop.bookshop.services.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

@Controller
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private BookService bookService;

    @GetMapping({"/vnpay"})
    public String home(){
        return "vnpay";
    }

    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam("amount") double amount,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request){
        int orderTotal = (int) amount;
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model){
        int paymentStatus =vnPayService.orderReturn(request);
        User currentUser = new User();
        HttpSession session = request.getSession();
        long id = (Long) session.getAttribute("id");
        currentUser.setId(id);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        if (paymentStatus == 1){
            PromotionDTO promotion = (PromotionDTO) session.getAttribute("promotion");
            String orderReceiverName = (String) session.getAttribute("orderReceiverName");
            String orderReceiverAdress = (String) session.getAttribute("orderReceiverAdress");
            String orderReceiverEmail = (String) session.getAttribute("orderReceiverEmail");
            String orderReceiverPhone = (String) session.getAttribute("orderReceiverPhone");
            Order order = bookService.placeOrderVnPay(currentUser, session,
                                                      orderReceiverName,orderReceiverAdress,
                                                      orderReceiverEmail,orderReceiverPhone,
                                                      promotion);
            return "ordersuccess";
        }
        else {
            return "orderfail";
        }
    }
}
