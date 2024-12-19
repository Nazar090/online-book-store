package com.example.onlinebookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinebookstore.dto.category.CategoryDto;
import com.example.onlinebookstore.dto.category.CategoryRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql(scripts = "classpath:database/add-books-with-categories.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/cleanup-tables.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCategory_ShouldCreateAndReturnCategory() throws Exception {
        // Given
        CategoryRequestDto requestDto = new CategoryRequestDto()
                .setName("New Category")
                .setDescription("New Description");

        // When
        var result = mockMvc.perform(post("/categories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        CategoryDto actualCategory = objectMapper.readValue(jsonResponse, CategoryDto.class);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals("New Category", actualCategory.name());
        Assertions.assertEquals("New Description", actualCategory.description());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllCategories_ShouldReturnAllCategories() throws Exception {
        // When
        var result = mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        List<CategoryDto> actualCategories = objectMapper.readValue(
                jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, CategoryDto.class)
        );

        Assertions.assertFalse(actualCategories.isEmpty(), "The category list should not be empty");
        Assertions.assertEquals(3, actualCategories.size(),
                "There should be 3 categories in the list");
    }

    @Test
    @WithMockUser(roles = "USER")
    void getCategoryById_ShouldReturnCategory() throws Exception {
        // Given
        Long categoryId = 3L;
        String expectedName = "Science Fiction";

        // When
        var result = mockMvc.perform(get("/categories/{id}", categoryId))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        CategoryDto actualCategory = objectMapper.readValue(jsonResponse, CategoryDto.class);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(expectedName, actualCategory.name());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategory_ShouldUpdateAndReturnCategory() throws Exception {
        // Given
        Long categoryId = 1L;
        CategoryRequestDto requestDto = new CategoryRequestDto()
                .setName("Updated Category")
                .setDescription("Updated Description");

        // When
        var result = mockMvc.perform(put("/categories/{id}", categoryId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        CategoryDto actualCategory = objectMapper.readValue(jsonResponse, CategoryDto.class);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals("Updated Category", actualCategory.name());
        Assertions.assertEquals("Updated Description", actualCategory.description());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCategory_ShouldReturnNoContent() throws Exception {
        // Given
        Long categoryId = 3L;

        // When
        mockMvc.perform(delete("/categories/{id}", categoryId))
                .andExpect(status().isNoContent());
    }
}
