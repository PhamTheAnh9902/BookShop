package com.shop.bookshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "promotions")
@Getter
@Setter
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long promotionId;
    private String promotionName;

    private String code;

    private double discountRate;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(mappedBy = "promotion")
    private List<Order> orders;
}
