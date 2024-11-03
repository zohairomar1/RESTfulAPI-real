package com.zohair.RESTfulAPI.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zohair.RESTfulAPI.TestDataUtil;
import com.zohair.RESTfulAPI.domain.entities.AuthorEntity;
import com.zohair.RESTfulAPI.services.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc // create mockmvc instances and place into application context
public class AuthorControllerIntegrationTests {

    private AuthorService authorService;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, AuthorService authorService) {
        this.authorService = authorService;
        this.objectMapper =  new ObjectMapper();
        this.mockMvc = mockMvc;
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception {
        AuthorEntity testAuthor = TestDataUtil.createTestAuthorA();
        testAuthor.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthor);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson))
                .andExpect(
                        MockMvcResultMatchers.status().isCreated()
                );
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        AuthorEntity testAuthor = TestDataUtil.createTestAuthorA();
        testAuthor.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthor);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/authors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").isNumber() // id is generated automatically in jpa
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.name").value(testAuthor.getName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.age").value(testAuthor.getAge())
                );
    }

    @Test
    public void testThatListAuthorReturnsHttp200() throws Exception {


        mockMvc.perform(
                        MockMvcRequestBuilders.get("/authors")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    public void testThatListAuthorReturnsListOfAuthors() throws Exception {

        AuthorEntity testAuthor = TestDataUtil.createTestAuthorA();
        authorService.createAuthor(testAuthor);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/authors")
                                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].id").isNumber() // id is generated automatically in jpa
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].name").value(testAuthor.getName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].age").value(testAuthor.getAge())
                );
    }

    @Test
    public void testThatReadOneAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {

        authorService.createAuthor(TestDataUtil.createTestAuthorA());

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/authors/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    public void testThatReadOneAuthorReturnsHttpStatus404WhenAuthorDoesNotExist() throws Exception {


        mockMvc.perform(
                        MockMvcRequestBuilders.get("/authors/99")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound()
                );
    }

    @Test
    public void testThatReadOneAuthorReturnsAuthor() throws Exception {

        AuthorEntity author = authorService.createAuthor(TestDataUtil.createTestAuthorA());

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/authors/1")
                                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").value(1)
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.name").value(author.getName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.age").value(author.getAge())
                );
    }

    @Test
    public void testThatUpdateAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {

        AuthorEntity authorEntity = authorService.createAuthor(TestDataUtil.createTestAuthorA());
        String authorJson = objectMapper.writeValueAsString(authorEntity);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/authors/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    public void testThatUpdateAuthorReturnsHttpStatus404WhenAuthorDoesNotExist() throws Exception {

        AuthorEntity authorEntity = authorService.createAuthor(TestDataUtil.createTestAuthorA());
        String authorJson = objectMapper.writeValueAsString(authorEntity);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/authors/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound()
                );
    }

    @Test
    public void testThatUpdateAuthorUpdatesExistingAuthor() throws Exception {

        AuthorEntity authorEntity = authorService.createAuthor(TestDataUtil.createTestAuthorA());
        authorEntity.setName("not the name");
        authorEntity.setAge(10);
        String authorJson = objectMapper.writeValueAsString(authorEntity);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/authors/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson))

                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").value(1)
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.name").value(authorEntity.getName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.age").value(authorEntity.getAge())
                );
    }

    @Test
    public void testThatUpdateAuthorDifferentIDInJSONBodyUpdatesAuthorWithPathIDInstead() throws Exception {

        AuthorEntity authorEntity = authorService.createAuthor(TestDataUtil.createTestAuthorA());
        authorEntity.setName("not the name");
        authorEntity.setAge(10);
        authorEntity.setId(20L);
        String authorJson = objectMapper.writeValueAsString(authorEntity);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/authors/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson))

                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").value(1)
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.name").value(authorEntity.getName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.age").value(authorEntity.getAge())
                );
    }

    @Test
    public void testThatPartialUpdateAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception {

        AuthorEntity authorEntity = authorService.createAuthor(TestDataUtil.createTestAuthorA());
        authorEntity.setName("this is an updated name");
        String authorJson = objectMapper.writeValueAsString(authorEntity);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/authors/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    public void testThatPartialUpdateAuthorReturnsHttpStatus404WhenAuthorDoesNotExist() throws Exception {

        AuthorEntity authorEntity = authorService.createAuthor(TestDataUtil.createTestAuthorA());
        authorEntity.setName("this is an updated name");
        String authorJson = objectMapper.writeValueAsString(authorEntity);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/authors/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound()
                );
    }

    @Test
    public void testThatPartialUpdateAuthorUpdatesSingleValue() throws Exception {

        AuthorEntity authorEntity = authorService.createAuthor(TestDataUtil.createTestAuthorA());
        authorEntity.setAge(10);

        String authorJson = objectMapper.writeValueAsString(authorEntity);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/authors/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(authorJson))

                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").value(1)
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.name").value(authorEntity.getName())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.age").value(10)
                );
    }

}
