package com.shop.bookshop.repository;

import com.shop.bookshop.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRespository extends JpaRepository<Book,Long> {
    List<Book> findBookByTitleContainingIgnoreCase(String query);
}
