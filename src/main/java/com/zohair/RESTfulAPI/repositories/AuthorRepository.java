package com.zohair.RESTfulAPI.repositories;

import com.zohair.RESTfulAPI.domain.entities.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long>,
        PagingAndSortingRepository<AuthorEntity, Long> {
}
