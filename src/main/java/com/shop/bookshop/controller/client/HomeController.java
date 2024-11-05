package com.shop.bookshop.controller.client;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.shop.bookshop.domain.User;
import com.shop.bookshop.domain.Dto.UserRegistrationDto;
import com.shop.bookshop.services.UserService;

import jakarta.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    // HOME
    @GetMapping("/")
    public String homepage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            model.addAttribute("userEmail", userDetails.getUsername());
        } else {
            model.addAttribute("userEmail", null);
        }
        return "index";
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
