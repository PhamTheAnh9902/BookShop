package com.shop.bookshop.controller.admin;

import com.shop.bookshop.domain.Author;
import com.shop.bookshop.domain.Promotion;
import com.shop.bookshop.services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class PromotionController {
    @Autowired
    PromotionService promotionService;

    //LIST
    @RequestMapping("/promotion/{pageNum}")
    public String getAllPromotionPaging(@PathVariable("pageNum") int pageNum, Model model){

        Page<Promotion> page = promotionService.getAllPromotionPaging(pageNum);
        List<Promotion> promotions = page.getContent();
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("promotions", promotions);
        return "admin/promotion/list_promotion";
    }

    //CREATE
    @GetMapping("/promotion/add")
    public String getAddPromotionPage(Model model) {
        model.addAttribute("promotion", new Promotion());
        return "/admin/promotion/add_promotion";
    }
    @PostMapping("/promotion/add")
    public String createNewPromotion(@ModelAttribute("promotion") Promotion promotion, RedirectAttributes redirectAttributes) {
        LocalDate startDate = promotion.getStartDate();
        LocalDate endDate = promotion.getEndDate();

        if (startDate.isAfter(endDate)) {
            redirectAttributes.addFlashAttribute("message", "Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu.");
            return "redirect:/admin/promotion/1";
        }

        Promotion createdPromotion = promotionService.createPromotion(promotion);
        if (createdPromotion != null) {
            return "redirect:/admin/promotion/1";
        } else {
            return "admin/promotion/add_promotion";
        }
    }

    //UPDATE
    @GetMapping("/promotion/update/{id}")
    public String updateAuthor(Model model, @PathVariable long id) {
        Promotion promotion = promotionService.getPromotionById(id);
        model.addAttribute("promotion", promotion);
        return "admin/promotion/update_promotion";
    }
    @PostMapping("/promotion/update/{id}")
    public String updateBook(@PathVariable("id") Long id, @ModelAttribute("promotion") Promotion promotion) {
        Promotion createdPromotion = promotionService.updatePromotion(id,promotion);

        if (createdPromotion != null) {
            return "redirect:/admin/promotion/1";
        } else {
            return "admin/promotion/update_promotion";
        }
    }

    //DELETE
    @GetMapping("/promotion/delete/{id}")
    public String deletePromotion(@PathVariable long id) {
        promotionService.deletePromotionById(id);
        return "redirect:/admin/promotion/1";
    }
}
