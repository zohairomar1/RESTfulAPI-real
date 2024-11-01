package com.zohair.RESTfulAPI.services;

import com.zohair.RESTfulAPI.domain.entities.AuthorEntity;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    AuthorEntity createAuthor(AuthorEntity authorEntity);

    List<AuthorEntity> findAll();

    Optional<AuthorEntity> findOne(Long id);

    boolean doesExist(Long id);

    AuthorEntity updateAuthor(AuthorEntity authorEntity);
}
