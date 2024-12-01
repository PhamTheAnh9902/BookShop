package com.shop.bookshop.controller.admin;

import com.shop.bookshop.domain.*;
import com.shop.bookshop.services.BookService;
import com.shop.bookshop.services.PublisherService;
import com.shop.bookshop.services.PurchaseOrderService;
import com.shop.bookshop.services.UserService;
import com.shop.bookshop.util.constant.FormatterUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderService purchaseOrderService;
    @Autowired
    private PublisherService publisherService;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @Autowired
    private FormatterUtil formatterUtil;

    //LIST
    @RequestMapping("/purchase/{pageNum}")
    public String getAllPurchaseOrderPaging(Model model, @PathVariable("pageNum") int pageNum) {
        Page<PurchaseOrder> page = purchaseOrderService.getAllBookPaging(pageNum,4);
        List<PurchaseOrder> purchaseOrders = page.getContent();

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("purchaseOrders", purchaseOrders);
        model.addAttribute("formatter", formatterUtil);

        return "admin/purchase/list_purchase";
    }

    //CREATE
    @GetMapping("/purchase/add")
    public String getAddPurchaseOrderPaging(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        long id = (Long) session.getAttribute("id");
        User user = userService.getUserById(id);
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        List<Publisher> publishers = publisherService.getAllPublishers();
        List<Book> books = bookService.getAllBook();

        model.addAttribute("user",user);
        model.addAttribute("purchaseOrder",purchaseOrder);
        model.addAttribute("publishers",publishers);
        model.addAttribute("books",books);

        return "admin/purchase/add_purchase";
    }

    @PostMapping("/purchase/add")
    public String AddPurchaseOrder(@ModelAttribute("purchaseOrder") PurchaseOrder purchaseOrder, HttpServletRequest request){
        HttpSession session = request.getSession();
        PurchaseOrder currentPurchaseOrder = purchaseOrderService.addPurchaseOrder(purchaseOrder, session);
        return "redirect:/admin/purchase/1";
    }

    //UPDATE
    @GetMapping("purchase/update/{id}")
    public String updatePurchaseOrder(Model model, @PathVariable long id) {
        PurchaseOrder purchaseOrder = purchaseOrderService.findById(id);
        User user = userService.getUserById(purchaseOrder.getEnteredBy().getId());
        List<Publisher> publishers = publisherService.getAllPublishers();
        List<Book> books = bookService.getAllBook();

        model.addAttribute("purchaseOrder",purchaseOrder);
        model.addAttribute("user",user);
        model.addAttribute("publishers",publishers);
        model.addAttribute("books",books);
        model.addAttribute("formatter", formatterUtil);
        return "admin/purchase/update_purchase";
    }

    @PostMapping("/purchase/update/{id}")
    public String updateOrder(@PathVariable("id") Long id, @ModelAttribute("purchaseOrder") PurchaseOrder purchaseOrder) {
        PurchaseOrder createdPurchaseOrder = purchaseOrderService.updatePurchaseOrder(id,purchaseOrder);
        if (createdPurchaseOrder != null) {
            return "redirect:/admin/purchase/1";
        } else {
            return "admin/purchase/update_purchase";
        }
    }
    //DELETE
    @GetMapping("/purchase/delete/{id}")
    public String deleteOrder(@PathVariable long id, RedirectAttributes redirectAttributes){
        boolean check = purchaseOrderService.deletePurchaseOrder(id);
        if (check == false){
            redirectAttributes.addFlashAttribute("message","Có lỗi trong quá trình xóa phiếu!");
            return "redirect:/admin/purchase/1";
        }
        return "redirect:/admin/purchase/1";
    }
}
