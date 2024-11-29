package com.shop.bookshop.controller.client;

import com.shop.bookshop.domain.Book;
import com.shop.bookshop.domain.Category;
import com.shop.bookshop.domain.Review;
import com.shop.bookshop.services.BookService;
import com.shop.bookshop.services.CategoryService;
import com.shop.bookshop.services.ReviewService;
import com.shop.bookshop.util.constant.FormatterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BooksController {

    @Autowired
    BookService bookService;

    @Autowired
    CategoryService categoryService;
    @Autowired
    private FormatterUtil formatterUtil;
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/books")
    public String getBookPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Book> books = bookService.getAllBook();
        Collections.shuffle(books);
        List<Book> bestSellerBooks = books.stream().limit(4).collect(Collectors.toList());
        List<Category> categories =  categoryService.getAllCategory();
        model.addAttribute("books", books);
        model.addAttribute("bestSellerBooks", bestSellerBooks);
        model.addAttribute("categories",categories);
        model.addAttribute("formatter", formatterUtil);
        model.addAttribute("sortBy", "Mặc định");
        if (userDetails != null) {
            model.addAttribute("userEmail", userDetails.getUsername());
        } else {
            model.addAttribute("userEmail", null);
        }
        return "books";
    }

    @GetMapping("/detail/{id}")
    public String getBookDetailPage(@AuthenticationPrincipal UserDetails userDetails,
                                    @PathVariable Long id, Model model){
        Book book = bookService.getBookById(id);
        List<Book> suggestBooks = bookService.getAllBook().subList(0,5);
        List<Review> reviews = reviewService.getReviewByBookId(book);
        DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        model.addAttribute("book", book);
        model.addAttribute("suggestBooks", suggestBooks);
        model.addAttribute("formatter", formatterUtil);
        model.addAttribute("formatDateTime", formatDateTime);
        model.addAttribute("reviews", reviews);
        if (userDetails != null) {
            model.addAttribute("userEmail", userDetails.getUsername());

        } else {
            model.addAttribute("userEmail", null);
        }
        return "book_detail";
    }



    //SEARCH
    @GetMapping("/books/search")
    public String searchBookByName(@AuthenticationPrincipal UserDetails userDetails,
                                   @RequestParam("query") String query, Model model) {
        List<Book> books = bookService.searchBookByName(query);
        List<Category> categories =  categoryService.getAllCategory();
        Collections.shuffle(bookService.getAllBook());
        List<Book> bestSellerBooks = books.stream().limit(4).collect(Collectors.toList());
        model.addAttribute("books", books);
        model.addAttribute("bestSellerBooks", bestSellerBooks);
        model.addAttribute("query", query);
        model.addAttribute("categories",categories);
        model.addAttribute("formatter", formatterUtil);
        model.addAttribute("sortBy", "Mặc định");
        if (userDetails != null) {
            model.addAttribute("userEmail", userDetails.getUsername());
        } else {
            model.addAttribute("userEmail", null);
        }
        return "books";
    }

    //SORT
    @GetMapping("/books/sorted")
    public String getSortedBooks(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam String sortBy, Model model) {
        Sort sort;
        switch (sortBy) {
            case "titleAsc":
                sort = Sort.by(Sort.Direction.ASC, "title");
                model.addAttribute("sortBy", "Tên A-Z");
                break;
            case "titleDesc":
                sort = Sort.by(Sort.Direction.DESC, "title");
                model.addAttribute("sortBy", "Tên Z-A");
                break;
            case "priceAsc":
                sort = Sort.by(Sort.Direction.ASC, "price");
                model.addAttribute("sortBy", "Giá tăng dần");
                break;
            case "priceDesc":
                sort = Sort.by(Sort.Direction.DESC, "price");
                model.addAttribute("sortBy", "Giá giảm dần");
                break;
            default:
                sort = Sort.by(Sort.Direction.ASC, "name");
                model.addAttribute("sortBy", "Mặc định");
                break;
        }
        List<Category> categories = categoryService.getAllCategory();
        List<Book> books = bookService.getAllBooksSorted(sort);
        Collections.shuffle(bookService.getAllBook());
        List<Book> bestSellerBooks = bookService.getAllBook().stream().limit(4).collect(Collectors.toList());
        model.addAttribute("books", books);
        model.addAttribute("bestSellerBooks", bestSellerBooks);
        model.addAttribute("categories",categories);
        model.addAttribute("formatter", formatterUtil);
        if (userDetails != null) {
            model.addAttribute("userEmail", userDetails.getUsername());
        } else {
            model.addAttribute("userEmail", null);
        }
        return "books";
    }

    @GetMapping("/books/{id}")
    public String getBooksByCategory(@AuthenticationPrincipal UserDetails userDetails, Model model,
                                     @PathVariable("id") long id){
        List<Category> categories = categoryService.getAllCategory();
        List<Book> booksByCategory = bookService.findByCategoryId(id);
        Collections.shuffle(bookService.getAllBook());
        List<Book> bestSellerBooks = bookService.getAllBook().stream().limit(4).collect(Collectors.toList());
        model.addAttribute("books", booksByCategory);
        model.addAttribute("bestSellerBooks", bestSellerBooks);
        model.addAttribute("categories",categories);
        model.addAttribute("formatter", formatterUtil);
        model.addAttribute("sortBy", "Mặc định");
        if (userDetails != null) {
            model.addAttribute("userEmail", userDetails.getUsername());
        } else {
            model.addAttribute("userEmail", null);
        }
        return "books";
    }
}
