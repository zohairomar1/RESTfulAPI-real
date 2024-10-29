package com.zohair.RESTfulAPI.repositories;

import com.zohair.RESTfulAPI.domain.entities.AuthorEntity;
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
public class AuthorEntityRepositoryIntegrationTests {

    private final AuthorRepository underTest;

    @Autowired
    public AuthorEntityRepositoryIntegrationTests(final AuthorRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testCreateAuthorWithId() {
        final AuthorEntity authorEntity = createTestAuthorA();
        final AuthorEntity savedAuthorEntity = underTest.save(authorEntity);
        assertThat(authorEntity).isEqualTo(savedAuthorEntity);
    }

    @Test
    public void testCreateAuthorWithoutId() {
        final AuthorEntity authorEntity = createTestAuthorA();
        authorEntity.setId(null);
        final AuthorEntity savedAuthorEntity = underTest.save(authorEntity);
        assertThat(authorEntity).isEqualTo(savedAuthorEntity);
    }

    @Test
    public void testCreateAndFindAuthorById() {
        final AuthorEntity authorEntity = createTestAuthorB();
        final AuthorEntity savedAuthorEntity = underTest.save(authorEntity);
        final Optional<AuthorEntity> result = underTest.findById(savedAuthorEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedAuthorEntity);
    }

    @Test
    public void testCreateAndFindAllAuthors() {
        final AuthorEntity testAuthorEntityA = underTest.save(createTestAuthorA());
        final AuthorEntity testAuthorEntityB = underTest.save(createTestAuthorB());
        final AuthorEntity testAuthorEntityC = underTest.save(createTestAuthorC());
        final List<AuthorEntity> expected = List.of(testAuthorEntityA, testAuthorEntityB, testAuthorEntityC);

        final List<AuthorEntity> result = underTest.findAll();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testCreateUpdateAuthor() {
        final AuthorEntity testAuthorEntityA = underTest.save(createTestAuthorA());
        testAuthorEntityA.setName("Updated");
        underTest.save(testAuthorEntityA);
        final Optional<AuthorEntity> result = underTest.findById(testAuthorEntityA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testAuthorEntityA);
    }

    @Test
    public void testCreateDeleteAuthor() {
        final AuthorEntity testAuthorEntityA = underTest.save(createTestAuthorA());
        final Optional<AuthorEntity> saveResult = underTest.findById(testAuthorEntityA.getId());
        assertThat(saveResult).isPresent();

        underTest.deleteById(testAuthorEntityA.getId());
        final Optional<AuthorEntity> result = underTest.findById(testAuthorEntityA.getId());
        assertThat(result).isNotPresent();
    }

}
