package com.shop.bookshop.controller.client;

import com.shop.bookshop.domain.*;
import com.shop.bookshop.domain.Dto.PromotionDTO;
import com.shop.bookshop.services.BookService;
import com.shop.bookshop.services.CategoryService;
import com.shop.bookshop.services.OrderService;
import com.shop.bookshop.services.PromotionService;
import com.shop.bookshop.util.constant.FormatterUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Autowired
    BookService bookService;
    @Autowired
    private FormatterUtil formatterUtil;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PromotionService promotionService;

    @GetMapping("/order")
    public String getOrderPage(@AuthenticationPrincipal UserDetails userDetails,
                               Model model, HttpServletRequest request){

        User currentUser = new User();
        HttpSession session = request.getSession();
        long id = (Long) session.getAttribute("id");
        currentUser.setId(id);

        List<Category> categories =  categoryService.getAllCategory();
        Cart cart = bookService.findByUser(currentUser);
        List<CartDetail> cartDetails = cart.getCartDetails();

        double totalPrice = 0;
        for(CartDetail cd : cartDetails){
            totalPrice += cd.getPrice() * cd.getQuantity();
        }
        if (userDetails != null) {
            model.addAttribute("userEmail", userDetails.getUsername());
            model.addAttribute("cartDetails", cartDetails);
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("categories",categories);
            model.addAttribute("formatter", formatterUtil);
            model.addAttribute("errorMessage", null);
        } else {
            model.addAttribute("userEmail", null);
        }
        return "order";
    }

    @PostMapping("/place-order")
    String placeOrder(@AuthenticationPrincipal UserDetails userDetails,
                      HttpServletRequest request, Model model, RedirectAttributes redirectAttributes,
                      @RequestParam("receiverName") String receiverName,
                      @RequestParam("receiverEmail") String receiverEmail,
                      @RequestParam("receiverAdress") String receiverAdress,
                      @RequestParam("receiverPhone") String receiverPhone,
                      @RequestParam(value = "promoCode", required = false) String promoCode,
                      @RequestParam(value = "payment") String payment){

        User currentUser = new User();
        HttpSession session = request.getSession();
        long id = (Long) session.getAttribute("id");
        currentUser.setId(id);

        Promotion promotion = null;

        if (promoCode != null && !promoCode.isEmpty()){
            promotion = promotionService.applyPromotion(promoCode);
            if (promotion == null) {
                session.setAttribute("errorMessage", "Mã giảm giá không hợp lệ.");
                return "redirect:/order";
            }
        }
        if ("cod".equals(payment)){
            Order orderCheck = bookService.placeOrder(currentUser,session,
                    receiverName,receiverAdress,
                    receiverEmail,receiverPhone,
                    promotion);
            if (orderCheck != null) {
                return "redirect:/";
            } else {
                session.setAttribute("errorMessage", "Mã giảm giá không hợp lệ với điều kiện đơn hàng.");
                return "redirect:/order";
            }
        }
        else if ("vnpay".equals(payment)) {
            double amount = bookService.calculateOrderAmount(currentUser,promotion);
            String orderInfo= "Đơn hàng cho " + receiverName + ", Email: " + receiverEmail;
            PromotionDTO promotionDTO = new PromotionDTO();
            promotionDTO.setCode(promotion.getCode());
            promotionDTO.setDiscountRate(promotion.getDiscountRate());

            redirectAttributes.addFlashAttribute("totalPrices",amount);
            redirectAttributes.addFlashAttribute("orderInfo",orderInfo);
            session.setAttribute("orderReceiverName", receiverName);
            session.setAttribute("orderReceiverEmail", receiverEmail);
            session.setAttribute("orderReceiverAdress", receiverAdress);
            session.setAttribute("orderReceiverPhone", receiverPhone);
            session.setAttribute("promotion",promotionDTO);
            return "redirect:/vnpay";
        }
        else {
            session.setAttribute("errorMessage", "Phương thức thanh toán không hợp lệ.");
            return "redirect:/order";
        }
    }

    @GetMapping("/list_order")
    public String getListOrderPage(@AuthenticationPrincipal UserDetails userDetails,
                                   HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        long id = (Long) session.getAttribute("id");

        DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Order> orders = orderService.getOrderByUserId(id);
        Map<Order, Map<String, String>> orderBookNamesMap = new HashMap<>();
        for (Order order : orders) {
            List<OrderDetail> orderDetails = order.getOrderDetails();
            Map<String, String> bookNamesWithQuantity = new HashMap<>();

            for (OrderDetail orderDetail : orderDetails) {
                if (orderDetail.getBook() != null) {
                    String bookTitle = orderDetail.getBook().getTitle();
                    String quantity = String.valueOf(orderDetail.getQuantity());
                    bookNamesWithQuantity.put(bookTitle,quantity);
                }
            }
            orderBookNamesMap.put(order, bookNamesWithQuantity);
        }

        if (orders.isEmpty()){
            model.addAttribute("message","Bạn chưa có đơn hàng nào.");
        }
        if (userDetails != null) {
            model.addAttribute("userEmail", userDetails.getUsername());
            model.addAttribute("orders",orders);
            model.addAttribute("formatter", formatterUtil);
            model.addAttribute("formatDateTime",formatDateTime);
            model.addAttribute("orderBookNamesMap",orderBookNamesMap);
        } else {
            model.addAttribute("userEmail", null);
        }
        return "list_order";
    }
}
