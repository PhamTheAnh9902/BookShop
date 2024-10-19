package com.shop.bookshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String homepage() {
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

}
