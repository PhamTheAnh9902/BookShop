package com.shop.bookshop.services;

import com.shop.bookshop.domain.Book;
import com.shop.bookshop.domain.User;
import com.shop.bookshop.repository.BookRespository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookRespository bookRespository;

    public List<Book> getAllBook(){
        return bookRespository.findAll();
    }

    public Book createBook(Book book) {
        return bookRespository.save(book);
    }

    public Book getBookById(long id) {
        Optional<Book> bookOptional = bookRespository.findById(id);
        if (bookOptional.isPresent()) {
            return bookOptional.get();
        }
        return null;
    }

    public Book updateUser(Long id, Book book) {
        Book currentBook = this.getBookById(id);
        if (currentBook != null) {
            currentBook.setTitle(book.getTitle());
            currentBook.setQuantityInStock(book.getQuantityInStock());
            currentBook.setPrice(book.getPrice());
            currentBook.setImg(book.getImg());
            currentBook.setDescription(book.getDescription());
            currentBook.setCategory(book.getCategory());
            currentBook.setPublisher(book.getPublisher());
            currentBook.setAuthors(book.getAuthors());
        }
        return bookRespository.save(currentBook);
    }

    public boolean deleteUser(long id) {
        try {
            Book book = bookRespository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Book not found"));
            book.getAuthors().clear();
            bookRespository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
