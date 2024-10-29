package com.zohair.RESTfulAPI.controllers;

import com.zohair.RESTfulAPI.domain.dto.AuthorDto;
import com.zohair.RESTfulAPI.domain.entities.AuthorEntity;
import com.zohair.RESTfulAPI.mappers.Mapper;
import com.zohair.RESTfulAPI.services.AuthorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorController {

    private AuthorService authorService;
    private Mapper<AuthorEntity,AuthorDto> authorMapper;

    public AuthorController(AuthorService authorService, Mapper<AuthorEntity,AuthorDto> authorMapper) {
        this.authorMapper = authorMapper;
        this.authorService = authorService;
    }

    @PostMapping(path="/authors")
    public AuthorDto createAuthor(@RequestBody AuthorDto author) {
        AuthorEntity authorEntity = authorMapper.mapFrom(author);
        AuthorEntity savedAuthorEntity = authorService.createAuthor(authorEntity);
        return authorMapper.mapTo(savedAuthorEntity);
    }


}
