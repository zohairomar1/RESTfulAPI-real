package com.zohair.RESTfulAPI.repositories;

import com.zohair.RESTfulAPI.domain.entities.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
}
