package com.shop.bookshop.domain;

import com.shop.bookshop.util.constant.StatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    private String receiverEmail;
    private double totalPrice;
    private double discountValue;
    private String statusPayment;
    private LocalDateTime createDate;
    private String description;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;
}
