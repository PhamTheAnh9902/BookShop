package com.shop.bookshop.controller.admin;

import java.util.List;

import com.shop.bookshop.domain.Role;
import com.shop.bookshop.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Autowired
    private RoleService roleService;


    // LIST
    //        @GetMapping("/user")
    //        public String getAllUsers(Model model) {
    //            List<User> list = this.userService.getAllUsers();
    //            model.addAttribute("users", list);
    //            return "admin/user/list_user";
    //        }
    @GetMapping("/user/{pageNum}")
    public String getAllUsers(Model model, @PathVariable("pageNum") int pageNum) {
        Page<User> page = userService.getAllUsersPaging(pageNum);
        List<User> list = page.getContent();
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("users", list);
        return "admin/user/list_user";
    }


    // CREATE
    @GetMapping("/add")
    public String addUserForm(Model model) {
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("user", new User());
        model.addAttribute("roles", roles);
        return "/admin/user/add_user";
    }

    @PostMapping("/add")
    public String createNewUser(@ModelAttribute("user") User user) {
        User createdUser = this.userService.createUser(user);

        if (createdUser != null) {
            return "redirect:/admin/user/1";
        } else {
            return "admin/user/add_user";
        }
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable long id) {
        if (userService.deleteUser(id)) {
            return "redirect:/admin/user/1";
        } else {
            return "redirect:/admin/user/1";
        }
    }

    // UPDATE

    @GetMapping("/update/{id}")
    public String updateUser(Model model, @PathVariable long id) {
        User user = userService.getUserById(id);
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "admin/user/update_user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long userId, @ModelAttribute("user") User user) {
        User createdUser = userService.updateUser(userId,user);

        if (createdUser != null) {
            return "redirect:/admin/user/1";
        } else {
            return "admin/user/update_user";
        }
    }



}
