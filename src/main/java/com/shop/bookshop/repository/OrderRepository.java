package com.shop.bookshop.repository;

import com.shop.bookshop.domain.Order;
import com.shop.bookshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOrderByUserId(long id);

    int countByUser(User user);
}
