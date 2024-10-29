package com.zohair.RESTfulAPI.services;

import com.zohair.RESTfulAPI.domain.entities.AuthorEntity;

import java.util.List;

public interface AuthorService {

    AuthorEntity createAuthor(AuthorEntity authorEntity);

    List<AuthorEntity> findAll();
}
