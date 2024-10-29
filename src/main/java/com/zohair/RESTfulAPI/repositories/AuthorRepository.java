package com.zohair.RESTfulAPI.repositories;

import com.zohair.RESTfulAPI.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
