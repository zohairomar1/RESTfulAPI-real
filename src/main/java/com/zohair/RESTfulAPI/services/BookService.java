package com.zohair.RESTfulAPI.services;

import com.zohair.RESTfulAPI.domain.entities.BookEntity;

public interface BookService {

    BookEntity createBook(String isbn,BookEntity book);


}
