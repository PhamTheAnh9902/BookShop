package com.shop.bookshop.repository;

import com.shop.bookshop.domain.Cart;
import com.shop.bookshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
