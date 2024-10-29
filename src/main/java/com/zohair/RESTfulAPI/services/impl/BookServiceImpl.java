package com.zohair.RESTfulAPI.services.impl;

import com.zohair.RESTfulAPI.domain.entities.BookEntity;
import com.zohair.RESTfulAPI.repositories.BookRepository;
import com.zohair.RESTfulAPI.services.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookEntity createBook(String isbn, BookEntity book) {
        book.setIsbn(isbn); // make sure has isbn provided in the url, replace body if required
        return bookRepository.save(book);
    }
}
