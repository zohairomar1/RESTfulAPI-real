package com.zohair.RESTfulAPI.repositories;

import com.zohair.RESTfulAPI.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {

}
