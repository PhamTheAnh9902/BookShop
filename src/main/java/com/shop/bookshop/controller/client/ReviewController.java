package com.shop.bookshop.controller.client;

import com.shop.bookshop.domain.Book;
import com.shop.bookshop.domain.Review;
import com.shop.bookshop.domain.User;
import com.shop.bookshop.services.BookService;
import com.shop.bookshop.services.ReviewService;
import com.shop.bookshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ReviewController {
    @Autowired
    UserService userService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    BookService bookService;

    @PostMapping("/add-review/{id}")
    public String addReview(@AuthenticationPrincipal UserDetails userDetails,
                            @RequestParam("comment") String comment,
                            @PathVariable("id") long bookId, Model model){
        User user = userService.getUserByEmail(userDetails.getUsername());
        Book book = bookService.getBookById(bookId);
        Review review = new Review();
        review.setUser(user);
        review.setComment(comment);
        review.setBook(book);
        review.setCreateAt(LocalDateTime.now());
        reviewService.addReview(review);
        return "redirect:/detail/"+ bookId;
    }
}
