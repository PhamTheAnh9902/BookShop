package com.shop.bookshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "purchase_order_details")
@Getter
@Setter
public class PurchaseOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private double unitPrice;
    private double totalPrice;

    @ManyToOne
    @JoinColumn(name = "purchaseOrder_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
