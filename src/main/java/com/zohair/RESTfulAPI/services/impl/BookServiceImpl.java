package com.zohair.RESTfulAPI.services.impl;

import com.zohair.RESTfulAPI.domain.entities.BookEntity;
import com.zohair.RESTfulAPI.repositories.BookRepository;
import com.zohair.RESTfulAPI.services.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    @Override
    public List<BookEntity> findAll() {
        return StreamSupport
                .stream(bookRepository.findAll().spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookEntity> findOne(String isbn) {
        return bookRepository.findById(isbn);
    }

    @Override
    public boolean doesExist(String isbn) {
        return bookRepository.findById(isbn).isPresent();
    }
}
