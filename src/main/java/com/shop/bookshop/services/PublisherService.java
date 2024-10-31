package com.shop.bookshop.services;

import com.shop.bookshop.domain.Publisher;
import com.shop.bookshop.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {

    @Autowired
    PublisherRepository publisherRepository;

    public List<Publisher> getAllPublishers(){
        return publisherRepository.findAll();
    }

}
