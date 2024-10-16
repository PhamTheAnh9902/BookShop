package com.shop.bookshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.bookshop.domain.User;
import com.shop.bookshop.services.UserService;

@Controller
@RequestMapping("/admin")
public class UserController {

    @Autowired
    private UserService userService;

    // CREATE
    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "/admin/user/add_user";
    }

    @PostMapping("/add")
    public String createNewUser(@ModelAttribute("user") User user) {
        User createdUser = this.userService.createUser(user);

        if (createdUser != null) {
            return "redirect:/admin/user";
        } else {
            return "admin/user/add_user";
        }
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable long id) {
        if (userService.deleteUser(id)) {
            return "redirect:/admin/user";
        } else {
            return "redirect:/admin/user";
        }
    }

    // UPDATE

    @GetMapping("/update/{id}")
    public String updateUser(Model model, @PathVariable long id) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/user/update_user";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user) {
        User createdUser = this.userService.createUser(user);

        if (createdUser != null) {
            return "redirect:/admin/user";
        } else {
            return "admin/user/add_user";
        }
    }

    // LIST
    @GetMapping("/user")
    public String getAllUsers(Model model) {
        List<User> list = this.userService.getAllUsers();
        model.addAttribute("users", list);
        return "admin/user/list_user";
    }

}
