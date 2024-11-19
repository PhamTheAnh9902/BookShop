package com.shop.bookshop.controller.admin;

import com.shop.bookshop.domain.*;
import com.shop.bookshop.services.AuthorService;
import com.shop.bookshop.util.constant.FormatterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AuthorController {
    @Autowired
    AuthorService authorService;
    @Autowired
    private FormatterUtil formatterUtil;

    //LIST
    @RequestMapping("/author/{pageNum}")
    public String getAllOrderPaging(@PathVariable("pageNum") int pageNum, Model model){

        Page<Author> page = authorService.getAllAuthorPaging(pageNum);
        List<Author> authors = page.getContent();
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("authors", authors);
        model.addAttribute("formatter", formatterUtil);
        return "admin/author/list_author";
    }

    //CREATE
    @GetMapping("/author/add")
    public String getAddAuthorPage(Model model) {
        model.addAttribute("author", new Author());
        return "/admin/author/add_author";
    }
    @PostMapping("/author/add")
    public String createNewAuthor(@ModelAttribute("author") Author author) {
        Author createdAuthor = authorService.createAuthor(author);

        if (createdAuthor != null) {
            return "redirect:/admin/author/1";
        } else {
            return "admin/author/add_author";
        }
    }

    //UPDATE
    @GetMapping("/author/update/{id}")
    public String updateAuthor(Model model, @PathVariable long id) {
        Author author = authorService.getAuthorById(id);
        model.addAttribute("author", author);
        return "admin/author/update_author";
    }
    @PostMapping("/author/update/{id}")
    public String updateBook(@PathVariable("id") Long id, @ModelAttribute("author") Author author) {
        Author createdAuthor = authorService.updateAuthor(id,author);

        if (createdAuthor != null) {
            return "redirect:/admin/author/1";
        } else {
            return "admin/author/update_author";
        }
    }

    //DELETE
    @GetMapping("/author/delete/{id}")
    public String deleteAuthor(@PathVariable long id) {
        authorService.deleteAuThorById(id);
        return "redirect:/admin/author/1";
    }

}
