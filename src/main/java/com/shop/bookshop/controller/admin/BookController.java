package com.shop.bookshop.controller.admin;

import com.shop.bookshop.domain.*;
import com.shop.bookshop.services.AuthorService;
import com.shop.bookshop.services.BookService;
import com.shop.bookshop.services.CategoryService;
import com.shop.bookshop.services.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    PublisherService publisherService;

    @Autowired
    AuthorService authorService;

    //LIST
    //    @RequestMapping("/book")
    //    public String getAllBooks(Model model) {
    //        List<Book> books = bookService.getAllBook();
    //        model.addAttribute("books", books);
    //        return "admin/book/list_book";
    //    }
    @RequestMapping("/book/{pageNum}")
    public String getAllBookPaging(Model model, @PathVariable("pageNum") int pageNum) {

        Page<Book> page = bookService.getAllBookPaging(pageNum);
        List<Book> books = page.getContent();
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("books", books);
        return "admin/book/list_book";
    }

    //CREATE
    @GetMapping("/book/add")
    public String getAddBookForm(Model model) {
        List<Category> categories = categoryService.getAllCategory();
        List<Publisher> publishers = publisherService.getAllPublishers();
        List<Author> authors = authorService.getAllAuthors();
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authors);
        model.addAttribute("categories", categories);
        model.addAttribute("publishers", publishers);
        return "/admin/book/add_book";
    }

    @PostMapping("/book/add")
    public String createNewBook(@ModelAttribute("book") Book book) {
        Book createdBook = bookService.createBook(book);

        if (createdBook != null) {
            return "redirect:/admin/book";
        } else {
            return "admin/book/add_book";
        }
    }

    //UPDATE
    @GetMapping("book/update/{id}")
    public String updateBook(Model model, @PathVariable long id) {
        Book book = bookService.getBookById(id);
        List<Category> categories = categoryService.getAllCategory();
        List<Publisher> publishers = publisherService.getAllPublishers();
        List<Author> authors = authorService.getAllAuthors();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("categories", categories);
        model.addAttribute("publishers", publishers);
        return "admin/book/update_book";
    }

    @PostMapping("/book/update/{id}")
    public String updateBook(@PathVariable("id") Long id, @ModelAttribute("book") Book book) {
        Book createdBook = bookService.updateUser(id,book);

        if (createdBook != null) {
            return "redirect:/admin/book";
        } else {
            return "admin/book/update_book";
        }
    }

    //DELETE
    @GetMapping("/book/delete/{id}")
    public String deleteBook(@PathVariable long id) {
        if (bookService.deleteUser(id)) {
            return "redirect:/admin/book";
        } else {
            return "redirect:/admin/book";
        }
    }


}
