package com.shop.bookshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long purchaseOrderId;
    private LocalDateTime importDate;
    private int totalQuantity;
    private double totalValue;
    String status;
    String notes;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToOne
    @JoinColumn(name = "enteredBy")
    private User enteredBy;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    private List<PurchaseOrderDetail> purchaseOrderDetails;
}
