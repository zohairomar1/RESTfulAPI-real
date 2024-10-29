package com.zohair.RESTfulAPI.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zohair.RESTfulAPI.TestDataUtil;
import com.zohair.RESTfulAPI.domain.dto.BookDto;
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

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper) {
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
}
