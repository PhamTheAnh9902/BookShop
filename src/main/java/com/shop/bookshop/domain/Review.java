package com.shop.bookshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_reviews")
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long reviewId;

    @Column(length = 5000)
    private String comment;
    private int rating;
    private LocalDateTime createAt;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;




}
