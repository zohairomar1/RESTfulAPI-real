package com.zohair.RESTfulAPI.services;

import com.zohair.RESTfulAPI.domain.entities.AuthorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    AuthorEntity createAuthor(AuthorEntity authorEntity);

    List<AuthorEntity> findAll();

    Page<AuthorEntity> findAll(Pageable pageable);

    Optional<AuthorEntity> findOne(Long id);

    boolean doesExist(Long id);

    AuthorEntity updateAuthor(AuthorEntity authorEntity);

    AuthorEntity partialUpdate(Long id, AuthorEntity authorEntity);

    void deleteAuthor(Long id);
}
