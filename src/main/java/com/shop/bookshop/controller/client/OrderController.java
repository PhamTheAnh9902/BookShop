package com.shop.bookshop.controller.client;

import com.shop.bookshop.domain.Cart;
import com.shop.bookshop.domain.CartDetail;
import com.shop.bookshop.domain.Category;
import com.shop.bookshop.domain.User;
import com.shop.bookshop.services.BookService;
import com.shop.bookshop.services.CategoryService;
import com.shop.bookshop.util.constant.FormatterUtil;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    BookService bookService;
    @Autowired
    private FormatterUtil formatterUtil;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/order")
    public String getOrderPage(@AuthenticationPrincipal UserDetails userDetails,
                               Model model, HttpServletRequest request){
        List<Category> categories =  categoryService.getAllCategory();
        User currentUser = new User();
        HttpSession session = request.getSession();
        long id = (Long) session.getAttribute("id");
        currentUser.setId(id);

        Cart cart = bookService.findByUser(currentUser);

        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();

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
        } else {
            model.addAttribute("userEmail", null);
        }
        return "order";
    }

    @PostMapping("/place-order")
    String placeOrder(@AuthenticationPrincipal UserDetails userDetails,
                      HttpServletRequest request,
                      @RequestParam("receiverName") String receiverName,
                      @RequestParam("receiverEmail") String receiverEmail,
                      @RequestParam("receiverAdress") String receiverAdress,
                      @RequestParam("receiverPhone") String receiverPhone){

        User currentUser = new User();
        HttpSession session = request.getSession();
        long id = (Long) session.getAttribute("id");
        currentUser.setId(id);

        bookService.placeOrder(currentUser,session, receiverName,receiverAdress,receiverEmail,receiverPhone);

        return "redirect:/";
    }
}
