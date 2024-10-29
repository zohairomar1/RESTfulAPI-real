package com.zohair.RESTfulAPI.repositories;


import com.zohair.RESTfulAPI.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.zohair.RESTfulAPI.TestDataUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookRepositoryIntegrationTests {

    private final BookRepository underTest;

    @Autowired
    public BookRepositoryIntegrationTests(final BookRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testCreateBook() {
        final Author author = createTestAuthorA();
        final Book book = createTestBookA(author);
        final Book result = underTest.save(book);
        assertThat(result).isEqualTo(book);
    }

    @Test
    public void testCreateAndFindBook() {
        final Author author = createTestAuthorA();
        final Book book = createTestBookA(author);
        final Book savedBook = underTest.save(book);
        final Optional<Book> result = underTest.findById(savedBook.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedBook);
    }

    @Test
    public void testCreateAndFindAllBook() {
        final Author author = createTestAuthorA();
        final Book savedBookA = underTest.save(createTestBookA(author));
        final Book savedBookB = underTest.save(createTestBookB(author));
        final Book savedBookC = underTest.save(createTestBookC(author));
        final List<Book> expected = List.of(savedBookA, savedBookB, savedBookC);

        final List<Book> result = underTest.findAll();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testCreateUpdateBook() {
        final Author author = createTestAuthorA();
        final Book savedBook = underTest.save(createTestBookA(author));
        savedBook.setTitle("A new title");
        underTest.save(savedBook);

        final Optional<Book> result = underTest.findById(savedBook.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedBook);
    }

    @Test
    public void testCreateDeleteBook() {
        final Author author = createTestAuthorA();
        final Book savedBook = underTest.save(createTestBookA(author));
        final Optional<Book> saveResult = underTest.findById(savedBook.getIsbn());
        assertThat(saveResult).isPresent();

        underTest.deleteById(savedBook.getIsbn());

        final Optional<Book> afterDeleteResult = underTest.findById(savedBook.getIsbn());
        assertThat(afterDeleteResult).isNotPresent();
    }

}
