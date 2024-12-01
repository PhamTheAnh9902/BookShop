package com.shop.bookshop.repository;

import com.shop.bookshop.domain.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    PurchaseOrder findPurchaseOrderByPurchaseOrderId(Long id);
}
