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
    public ResponseEntity<BookDto> createUpdateBook(
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto bookDto) {

        boolean bookExists = bookService.doesExist(isbn);

        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        BookEntity bookSaved = bookService.createBook(isbn, bookEntity);
        BookDto savedUpdatedBookDto = bookMapper.mapTo(bookSaved);

        if (bookExists) {
            return new ResponseEntity<>(savedUpdatedBookDto,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(savedUpdatedBookDto,HttpStatus.CREATED);
        }

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

    @PatchMapping("books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateBook(@PathVariable("isbn") String isbn,
                                                     @RequestBody BookDto bookDto) {
        if (!bookService.doesExist(isbn)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        BookEntity partialUpdatedBookEntity = bookService.partialUpdate(isbn, bookEntity);
        return new ResponseEntity<>(
                bookMapper.mapTo(partialUpdatedBookEntity),
                HttpStatus.OK);
    }
}
