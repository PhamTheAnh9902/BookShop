package com.shop.bookshop.services;

import com.shop.bookshop.domain.Author;
import com.shop.bookshop.domain.Book;
import com.shop.bookshop.repository.AuthorRespository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    @Autowired
    AuthorRespository authorRespository;

    public Page<Author> getAllAuthorPaging(int pageNum) {
        int pageSize = 5;

        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        return authorRespository.findAll(pageable);
    }

    public List<Author> getAllAuthors(){
        return  authorRespository.findAll();
    }

    public Author createAuthor(Author author) {
        return authorRespository.save(author);
    }

    public Author getAuthorById(long id) {
        Optional<Author> authorOptional = authorRespository.findById(id);
        if (authorOptional.isPresent()) {
            return authorOptional.get();
        }
        return null;
    }

    public Author updateAuthor(Long id, Author author) {
        Author currentAuthor = this.getAuthorById(id);
        if (currentAuthor != null) {
            currentAuthor.setAuthorName(author.getAuthorName());
            currentAuthor.setAddress(author.getAddress());
        }
        return authorRespository.save(currentAuthor);
    }



    public void deleteAuThorById(long id) {
        authorRespository.deleteById(id);
    }
}
