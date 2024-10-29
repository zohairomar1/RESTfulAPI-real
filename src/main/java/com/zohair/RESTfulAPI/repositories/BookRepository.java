package com.zohair.RESTfulAPI.repositories;

import com.zohair.RESTfulAPI.domain.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookEntity, String> {

}
