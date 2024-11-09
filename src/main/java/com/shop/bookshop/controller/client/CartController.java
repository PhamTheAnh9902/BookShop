package com.shop.bookshop.controller.client;

import com.shop.bookshop.domain.Cart;
import com.shop.bookshop.domain.CartDetail;
import com.shop.bookshop.domain.User;
import com.shop.bookshop.services.BookService;
import com.shop.bookshop.util.constant.FormatterUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
public class CartController {

    @Autowired
    BookService bookService;
    @Autowired
    private FormatterUtil formatterUtil;


    @GetMapping("/cart")
    public String getCartPage(@AuthenticationPrincipal UserDetails userDetails,Model model, HttpServletRequest request){
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
            model.addAttribute("formatter", formatterUtil);
        } else {
            model.addAttribute("userEmail", null);
        }
        return "cart";
    }

    @PostMapping("/add-book-to-cart/{id}")
    public String addBooktoCart(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id, Model model, HttpServletRequest request){
        HttpSession session = request.getSession(false);

        long bookId = id;
        String email = userDetails.getUsername();
            bookService.addBookToCart(email, bookId, session);
        if (userDetails != null) {
            model.addAttribute("userEmail", userDetails.getUsername());
        } else {
            model.addAttribute("userEmail", null);
        }
        return "redirect:/books";
    }

    @GetMapping("/delete-book-from-cart/{id}")
    public String deleteBookFromCart(@PathVariable long id, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        long cartDetailId = id;
        bookService.removeCartDetail(cartDetailId,session);
        return "redirect:/cart";
    }
}
