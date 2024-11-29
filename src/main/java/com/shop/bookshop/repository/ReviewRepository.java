package com.shop.bookshop.repository;

import com.shop.bookshop.domain.Book;
import com.shop.bookshop.domain.Review;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository  extends JpaRepository<Review, Long> {
    List<Review> findReviewByBook(Book book);
}
