package com.shop.bookshop.repository;

import com.shop.bookshop.domain.Book;
import com.shop.bookshop.domain.Cart;
import com.shop.bookshop.domain.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDetailRepository  extends JpaRepository<CartDetail, Long> {
    CartDetail findByCartAndBook(Cart cart, Book book);
}
