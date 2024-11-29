package com.shop.bookshop.controller.client;

import com.shop.bookshop.domain.Book;
import com.shop.bookshop.domain.Category;
import com.shop.bookshop.services.BookService;
import com.shop.bookshop.services.CategoryService;
import com.shop.bookshop.util.constant.FormatterUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.shop.bookshop.domain.User;
import com.shop.bookshop.domain.Dto.UserRegistrationDto;
import com.shop.bookshop.services.UserService;

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookService bookService;
    @Autowired
    private FormatterUtil formatterUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    // HOME
    @GetMapping("/")
    public String homepage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Category> categories =  categoryService.getAllCategory();
        List<Book> books = bookService.getAllBook();
        Collections.shuffle(books);
        List<Book> bestSellerBooks = books.stream().limit(6).collect(Collectors.toList());
        List<Book> suggestBook = books.stream().limit(4).collect(Collectors.toList());
        model.addAttribute("categories",categories);
        model.addAttribute("bestSellerBooks", bestSellerBooks);
        model.addAttribute("suggestBook", suggestBook);
        model.addAttribute("books",books);
        model.addAttribute("formatter", formatterUtil);
        if (userDetails != null) {
            model.addAttribute("userEmail", userDetails.getUsername());
        } else {
            model.addAttribute("userEmail", null);
        }
        return "index";
    }

    //ACCOUNT
    @GetMapping("/account")
    public String getAccountPage(@AuthenticationPrincipal UserDetails userDetails,Model model){
        User user = userService.getUserByEmail(userDetails.getUsername());
        if (userDetails != null){
            model.addAttribute("userEmail", userDetails.getUsername());
            model.addAttribute("user",user);
        }
        else {
            model.addAttribute("userEmail", null);
        }
        return "account";
    }

    @PostMapping("/change-password")
    public String updatePassword(@AuthenticationPrincipal UserDetails userDetails, Model model,
                                 @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirectAttributes){
        User user = userService.getUserByEmail(userDetails.getUsername());
        if (!passwordEncoder.matches(currentPassword, user.getPassword())){
            redirectAttributes.addFlashAttribute("error", "Mật khẩu hiện tại không đúng.");
            return "redirect:/account";
        }
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới và xác nhận không khớp.");
            return "redirect:/account";
        }
        userService.updatePassword(user,newPassword);
            redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công");
        return "redirect:/account";
    }

    // REGISTER
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("userRegistrationDto") @Valid UserRegistrationDto userRegistrationDto,
            BindingResult bindingResult) {

        // validate
        if (bindingResult.hasErrors()) {

            return "register";
        }

        User user = userService.registerDTOtoUser(userRegistrationDto);
        return "redirect:/login";
    }

    // LOGIN
    @GetMapping("/login")
    public String getLoginPage(Model model) {

        return "login";
    }

    //LOGOUT
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        var auth = request.getUserPrincipal();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, null);
        }

        return "redirect:/login";
    }

}
