package com.shop.bookshop.repository;

import com.shop.bookshop.domain.Order;
import com.shop.bookshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrderByUserId(long id);

    int countByUser(User user);

    List<Order> findByCreateDateBetween(LocalDateTime startDate, LocalDateTime endDate);

}
