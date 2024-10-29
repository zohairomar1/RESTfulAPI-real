package com.zohair.RESTfulAPI.repositories;


import com.zohair.RESTfulAPI.domain.entities.AuthorEntity;
import com.zohair.RESTfulAPI.domain.entities.BookEntity;
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
public class BookEntityRepositoryIntegrationTests {

    private final BookRepository underTest;

    @Autowired
    public BookEntityRepositoryIntegrationTests(final BookRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testCreateBook() {
        final AuthorEntity authorEntity = createTestAuthorA();
        final BookEntity bookEntity = createTestBookA(authorEntity);
        final BookEntity result = underTest.save(bookEntity);
        assertThat(result).isEqualTo(bookEntity);
    }

    @Test
    public void testCreateAndFindBook() {
        final AuthorEntity authorEntity = createTestAuthorA();
        final BookEntity bookEntity = createTestBookA(authorEntity);
        final BookEntity savedBookEntity = underTest.save(bookEntity);
        final Optional<BookEntity> result = underTest.findById(savedBookEntity.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedBookEntity);
    }

    @Test
    public void testCreateAndFindAllBook() {
        final AuthorEntity authorEntity = createTestAuthorA();
        final BookEntity savedBookEntityA = underTest.save(createTestBookA(authorEntity));
        final BookEntity savedBookEntityB = underTest.save(createTestBookB(authorEntity));
        final BookEntity savedBookEntityC = underTest.save(createTestBookC(authorEntity));
        final List<BookEntity> expected = List.of(savedBookEntityA, savedBookEntityB, savedBookEntityC);

        final List<BookEntity> result = underTest.findAll();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testCreateUpdateBook() {
        final AuthorEntity authorEntity = createTestAuthorA();
        final BookEntity savedBookEntity = underTest.save(createTestBookA(authorEntity));
        savedBookEntity.setTitle("A new title");
        underTest.save(savedBookEntity);

        final Optional<BookEntity> result = underTest.findById(savedBookEntity.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedBookEntity);
    }

    @Test
    public void testCreateDeleteBook() {
        final AuthorEntity authorEntity = createTestAuthorA();
        final BookEntity savedBookEntity = underTest.save(createTestBookA(authorEntity));
        final Optional<BookEntity> saveResult = underTest.findById(savedBookEntity.getIsbn());
        assertThat(saveResult).isPresent();

        underTest.deleteById(savedBookEntity.getIsbn());

        final Optional<BookEntity> afterDeleteResult = underTest.findById(savedBookEntity.getIsbn());
        assertThat(afterDeleteResult).isNotPresent();
    }

}
