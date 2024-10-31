package com.shop.bookshop.services;

import com.shop.bookshop.domain.Author;
import com.shop.bookshop.repository.AuthorRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    @Autowired
    AuthorRespository authorRespository;

    public List<Author> getAllAuthors(){
        return  authorRespository.findAll();
    }
}
