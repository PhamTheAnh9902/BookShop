package com.shop.bookshop.services;

import com.shop.bookshop.domain.Book;
import com.shop.bookshop.domain.Review;
import com.shop.bookshop.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public void addReview(Review review) {
        reviewRepository.save(review);
    }

    public List<Review> getReviewByBookId(Book book) {

        return reviewRepository.findReviewByBook(book);
    }
}
