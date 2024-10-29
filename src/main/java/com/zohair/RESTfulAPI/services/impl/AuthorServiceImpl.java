package com.zohair.RESTfulAPI.services.impl;


import com.zohair.RESTfulAPI.domain.entities.AuthorEntity;
import com.zohair.RESTfulAPI.repositories.AuthorRepository;
import com.zohair.RESTfulAPI.services.AuthorService;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override // pass through method
    public AuthorEntity createAuthor(AuthorEntity authorEntity) {
        return authorRepository.save(authorEntity);
    }
}
