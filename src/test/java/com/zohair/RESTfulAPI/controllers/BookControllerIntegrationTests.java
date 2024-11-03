package com.zohair.RESTfulAPI.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zohair.RESTfulAPI.TestDataUtil;
import com.zohair.RESTfulAPI.domain.dto.BookDto;
import com.zohair.RESTfulAPI.domain.entities.BookEntity;
import com.zohair.RESTfulAPI.services.BookService;
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
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class BookControllerIntegrationTests {

    private BookService bookService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, BookService bookService) {
        this.bookService = bookService;
        this.objectMapper = new ObjectMapper();
        this.mockMvc = mockMvc;
    }

    @Test
    public void testThatCreateBookReturnsHttpStatus201Created() throws Exception {

        BookDto bookDto = TestDataUtil.createTestDtoA(null);
        String createBookJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createBookJson))
                .andExpect(
                        MockMvcResultMatchers.status().isCreated()
                );

    }

    @Test
    public void testThatCreateBookReturnsCorrectBook() throws Exception {
        BookDto bookDto = TestDataUtil.createTestDtoA(null);
        String createBookJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createBookJson))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.author").doesNotExist()
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.getIsbn())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle())
                );

    }

    @Test
    public void testThatListBookSReturnsHttpStatus200Created() throws Exception {


        mockMvc.perform(
                        MockMvcRequestBuilders.get("/books")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                );

    }

    @Test
    public void testThatListBooksReturnsCorrectBook() throws Exception {

        BookEntity testBookA = TestDataUtil.createTestBookC(null);
        bookService.createBook(testBookA.getIsbn(), testBookA);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/books")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].isbn").value(testBookA.getIsbn())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].author").doesNotExist()
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].title").value(testBookA.getTitle())
                );

    }

    @Test
    public void testThatReadOneBookReturnsHttpStatus200WhenBookExists() throws Exception {
        BookEntity testBookC = TestDataUtil.createTestBookC(null);

        bookService.createBook(testBookC.getIsbn(), testBookC);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/books/" + testBookC.getIsbn())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                );

    }

    @Test
    public void testThatReadOneBookReturnsHttpStatus404WhenBookDoesNotExist() throws Exception {


        mockMvc.perform(
                        MockMvcRequestBuilders.get("/books/random-isbn")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound()
                );

    }

    @Test
    public void testThatUpdateBookReturnsHttpStatus200InsteadOf201Created() throws Exception {

        BookEntity bookEntity = TestDataUtil.createTestBookA(null);
        bookService.createBook(bookEntity.getIsbn(), bookEntity);

        BookDto bookDto = TestDataUtil.createTestDtoA(null);
        bookDto.setTitle(bookEntity.getTitle() + " Updated");
        String createBookJson = objectMapper.writeValueAsString(bookDto);



        mockMvc.perform(
                        MockMvcRequestBuilders.put("/books/978-1-2345-6789-0")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createBookJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                );

    }

    @Test
    public void testThatUpdateBookUpdatesBook() throws Exception {

        BookEntity bookEntity = TestDataUtil.createTestBookA(null);
        bookService.createBook(bookEntity.getIsbn(), bookEntity);

        BookDto bookDto = TestDataUtil.createTestDtoA(null);
        bookDto.setTitle(bookEntity.getTitle() + " Updated");
        bookDto.setIsbn("not actual isbn");
        String createBookJson = objectMapper.writeValueAsString(bookDto);



        mockMvc.perform(
                        MockMvcRequestBuilders.put("/books/978-1-2345-6789-0")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createBookJson))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.isbn").value(bookEntity.getIsbn())
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle())
                );

    }

    @Test
    public void testThatPartialUpdateBookReturnsHttpStatus200WhenBookExists() throws Exception {

        BookEntity bookEntity = TestDataUtil.createTestBookA(null);
        bookService.createBook(bookEntity.getIsbn(), bookEntity);
        bookEntity.setTitle("Updated");
        String createBookJson = objectMapper.writeValueAsString(bookEntity);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/books/978-1-2345-6789-0")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createBookJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                );

    }

    @Test
    public void testThatPartialUpdateBookReturnsHttpStatus404WhenBookDoesNotExist() throws Exception {

        BookEntity bookEntity = TestDataUtil.createTestBookA(null);
        bookService.createBook(bookEntity.getIsbn(), bookEntity);

        bookEntity.setTitle("Updated");
        String createBookJson = objectMapper.writeValueAsString(bookEntity);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/books/5555")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createBookJson))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound()
                );

    }
    @Test
    public void testThatPartialUpdateBookUpdatesTitleWhenBookExists() throws Exception {

        BookEntity bookEntity = TestDataUtil.createTestBookA(null);
        bookService.createBook(bookEntity.getIsbn(), bookEntity);
        bookEntity.setTitle("Updated");
        String createBookJson = objectMapper.writeValueAsString(bookEntity);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/books/978-1-2345-6789-0")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createBookJson))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.isbn").value(bookEntity.getIsbn())
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.title").value("Updated")
                );

    }

    @Test
    public void testThatDeleteBookReturnsHTTP204WhenBookExists() throws Exception {

        BookEntity bookEntity = TestDataUtil.createTestBookA(null);
        bookService.createBook(bookEntity.getIsbn(), bookEntity);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/books/" + bookEntity.getIsbn())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.status().isNoContent()
                );

    }

    @Test
    public void testThatDeleteBookReturnsHTTP404WhenBookDoesNotExist() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/books/999")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound()
                );

    }
}
