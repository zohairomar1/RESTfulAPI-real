package com.zohair.RESTfulAPI.controllers;

import com.zohair.RESTfulAPI.domain.dto.AuthorDto;
import com.zohair.RESTfulAPI.domain.entities.AuthorEntity;
import com.zohair.RESTfulAPI.mappers.Mapper;
import com.zohair.RESTfulAPI.repositories.AuthorRepository;
import com.zohair.RESTfulAPI.services.AuthorService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AuthorController {

    private final AuthorRepository authorRepository;
    private AuthorService authorService;
    private Mapper<AuthorEntity,AuthorDto> authorMapper;

    public AuthorController(AuthorService authorService, Mapper<AuthorEntity,AuthorDto> authorMapper, AuthorRepository authorRepository) {
        this.authorMapper = authorMapper;
        this.authorService = authorService;
        this.authorRepository = authorRepository;
    }

    @PostMapping(path="/authors")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto author) {
        AuthorEntity authorEntity = authorMapper.mapFrom(author);
        AuthorEntity savedAuthorEntity = authorService.createAuthor(authorEntity);
        return new ResponseEntity<>(authorMapper.mapTo(savedAuthorEntity), HttpStatus.CREATED); //  response entity allows for changing the status type returned

    }

    @GetMapping(path = "/authors")
    public List<AuthorDto> listAuthors() {
        List<AuthorEntity> authors = authorService.findAll();
        return authors.stream().map(authorMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> readOneAuthor(@PathVariable("id") Long id) {
        Optional<AuthorEntity> foundAuthor = authorService.findOne(id);
        return foundAuthor.map(authorEntity -> {
            AuthorDto authorDto = authorMapper.mapTo(authorEntity);
            return new ResponseEntity<>(authorDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path="/authors/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable("id") Long id, @RequestBody AuthorDto author) {

        if (!authorService.doesExist(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } else {

            author.setId(id);
            AuthorEntity authorEntity = authorMapper.mapFrom(author);
            AuthorEntity savedAuthorEntity = authorService.createAuthor(authorEntity);
            return new ResponseEntity<>
                    (authorMapper.mapTo(savedAuthorEntity)
                    , HttpStatus.OK);
        }
    }

    @PatchMapping(path="authors/{id}")
    public ResponseEntity<AuthorDto> partialUpdateAuthor(
            @PathVariable("id") Long id,
            @RequestBody AuthorDto author) {

        if (!authorService.doesExist(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AuthorEntity authorEntity = authorMapper.mapFrom(author);
        AuthorEntity updatedAuthor = authorService.partialUpdate(id, authorEntity);
        return new ResponseEntity<>(
                authorMapper.mapTo(updatedAuthor),
                HttpStatus.OK);

    }


    @DeleteMapping(path="authors/{id}")
    public ResponseEntity deleteAuthor(@PathVariable("id") Long id) {
        if (!authorService.doesExist(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        authorService.deleteAuthor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
