package com.zohair.RESTfulAPI.services;

import com.zohair.RESTfulAPI.domain.entities.BookEntity;

import java.util.List;

public interface BookService {

    BookEntity createBook(String isbn,BookEntity book);

    List<BookEntity> findAll();
}
