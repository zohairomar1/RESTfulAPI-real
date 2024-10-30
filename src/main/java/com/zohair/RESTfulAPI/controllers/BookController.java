package com.zohair.RESTfulAPI.controllers;

import com.zohair.RESTfulAPI.domain.dto.AuthorDto;
import com.zohair.RESTfulAPI.domain.dto.BookDto;
import com.zohair.RESTfulAPI.domain.entities.AuthorEntity;
import com.zohair.RESTfulAPI.domain.entities.BookEntity;
import com.zohair.RESTfulAPI.mappers.Mapper;
import com.zohair.RESTfulAPI.services.BookService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {
    private Mapper<BookEntity,BookDto> bookMapper;
    private BookService bookService;

    public BookController(Mapper<BookEntity, BookDto> bookMapper, BookService bookService) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @PutMapping("/books/{isbn}")
    public ResponseEntity<BookDto> createBook(
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto bookDto) {
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        BookEntity savedBook = bookService.createBook(isbn, bookEntity);
        return new ResponseEntity<>(bookMapper.mapTo(savedBook),HttpStatus.CREATED);
    }

    @GetMapping("books")
    public List<BookDto> listBooks() {
        List<BookEntity> books = bookService.findAll();
        return books
                .stream()
                .map(bookMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping("books/{isbn}")
    public ResponseEntity<BookDto> readOneBook(@PathVariable("isbn") String isbn) {

        Optional<BookEntity> foundBook = bookService.findOne(isbn);
        return foundBook.map(bookEntity -> {
            BookDto bookDto = bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
