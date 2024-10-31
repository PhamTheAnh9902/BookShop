package com.shop.bookshop.services;

import com.shop.bookshop.domain.Category;
import com.shop.bookshop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;


    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }
}
