package com.zohair.RESTfulAPI.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zohair.RESTfulAPI.TestDataUtil;
import com.zohair.RESTfulAPI.domain.entities.AuthorEntity;
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

import javax.swing.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc // create mockmvc instances and place into application context
public class AuthorControllerIntegrationTests {

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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
}
