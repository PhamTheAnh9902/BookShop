package com.shop.bookshop.controller.admin;

import com.shop.bookshop.domain.*;
import com.shop.bookshop.services.BookService;
import com.shop.bookshop.services.OrderService;
import com.shop.bookshop.util.constant.FormatterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminOrderController {

    @Autowired
    OrderService orderService;
    @Autowired
    private FormatterUtil formatterUtil;

    @RequestMapping("/order/{pageNum}")
    public String getAllOrderPaging(@PathVariable ("pageNum") int pageNum, Model model){

        Page<Order> page = orderService.getAllOrderPaging(pageNum);
        List<Order> orders = page.getContent();
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("orders", orders);
        model.addAttribute("formatter", formatterUtil);
        return "admin/order/list_order";
    }

    //UPDATE
    @GetMapping("order/update/{id}")
    public String updateOrder(Model model, @PathVariable long id) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        model.addAttribute("formatter", formatterUtil);
        return "admin/order/update_order";
    }

    @PostMapping("/order/update/{id}")
    public String updateOrder(@PathVariable("id") Long id, @ModelAttribute("order") Order order) {
        Order createdOrder = orderService.updateUser(id,order);

        if (createdOrder != null) {
            return "redirect:/admin/order/1";
        } else {
            return "admin/order/update_order";
        }
    }

    //DELETE
    @GetMapping("/order/delete/{id}")
    public String deleteOrder(@PathVariable long id){
        orderService.deleteOrderById(id);
        return "redirect:/admin/order/1";
    }
}
