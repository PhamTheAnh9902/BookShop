package com.shop.bookshop.repository;

import com.shop.bookshop.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRespository extends JpaRepository<Author, Long> {
}
