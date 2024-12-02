package com.example.onlinebookstore.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.onlinebookstore.dto.category.CategoryDto;
import com.example.onlinebookstore.dto.category.CategoryRequestDto;
import com.example.onlinebookstore.security.JwtUtil;
import com.example.onlinebookstore.service.BookService;
import com.example.onlinebookstore.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private BookService bookService;

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
    void createCategory_ShouldReturnCreatedCategory() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto()
                .setName("Fiction");

        CategoryDto expectedCategory = new CategoryDto(1L, "Fantasy", "description");

        Mockito.when(categoryService.save(any())).thenReturn(expectedCategory);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/categories")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CategoryDto actualCategory = objectMapper.readValue(jsonResponse, CategoryDto.class);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(expectedCategory.id(), actualCategory.id());
        Assertions.assertEquals(expectedCategory.name(), actualCategory.name());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAll_ShouldReturnPaginatedCategoryList() throws Exception {
        List<CategoryDto> expectedCategories = List.of(
                new CategoryDto(1L, "Fantasy", "description"),
                new CategoryDto(2L, "Horror", "description")
        );

        Mockito.when(categoryService.findAll(any())).thenReturn(expectedCategories);

        MvcResult result = mockMvc.perform(get("/categories")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<CategoryDto> actualCategories = objectMapper.readValue(
                jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, CategoryDto.class)
        );

        Assertions.assertNotNull(actualCategories);
        Assertions.assertEquals(expectedCategories.size(), actualCategories.size());
        Assertions.assertEquals(expectedCategories.get(0).id(), actualCategories.get(0).id());
        Assertions.assertEquals(expectedCategories.get(1).name(), actualCategories.get(1).name());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getCategoryById_ShouldReturnCategory() throws Exception {
        Long categoryId = 1L;
        CategoryDto expectedCategory = new CategoryDto(1L, "Fantasy", "description");

        Mockito.when(categoryService.getById(eq(categoryId))).thenReturn(expectedCategory);

        MvcResult result = mockMvc.perform(get("/categories/{id}", categoryId))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CategoryDto actualCategory = objectMapper.readValue(jsonResponse, CategoryDto.class);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(expectedCategory.id(), actualCategory.id());
        Assertions.assertEquals(expectedCategory.name(), actualCategory.name());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategory_ShouldReturnUpdatedCategory() throws Exception {
        Long categoryId = 1L;
        CategoryRequestDto requestDto = new CategoryRequestDto()
                .setName("Updated Fiction");

        CategoryDto updatedCategory = new CategoryDto(1L, "Fantasy", "description");

        Mockito.when(categoryService.update(eq(categoryId), any())).thenReturn(updatedCategory);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/categories/{id}", categoryId)
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CategoryDto actualCategory = objectMapper.readValue(jsonResponse, CategoryDto.class);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(updatedCategory.id(), actualCategory.id());
        Assertions.assertEquals(updatedCategory.name(), actualCategory.name());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCategory_ShouldReturnNoContent() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(delete("/categories/{id}", categoryId))
                .andExpect(status().isNoContent());

        Mockito.verify(categoryService).deleteById(categoryId);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBookWithoutCategories_ShouldReturnBooksInCategory() throws Exception {
        Long categoryId = 1L;
        List<BookDtoWithoutCategoryIds> expectedBooks = List.of(
                new BookDtoWithoutCategoryIds()
                        .setId(1L)
                        .setTitle("Book 1")
                        .setAuthor("Author 1")
                        .setIsbn("12345"),
                new BookDtoWithoutCategoryIds()
                        .setId(2L)
                        .setTitle("Book 2")
                        .setAuthor("Author 2")
                        .setIsbn("67890")
        );

        Mockito.when(bookService.findAllByCategoryId(eq(categoryId))).thenReturn(expectedBooks);

        MvcResult result = mockMvc.perform(get("/categories/{id}/books", categoryId))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<BookDtoWithoutCategoryIds> actualBooks = objectMapper.readValue(
                jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class,
                        BookDtoWithoutCategoryIds.class)
        );

        Assertions.assertNotNull(actualBooks);
        Assertions.assertEquals(expectedBooks.size(), actualBooks.size());
        Assertions.assertEquals(expectedBooks.get(0).getId(), actualBooks.get(0).getId());
        Assertions.assertEquals(expectedBooks.get(1).getTitle(), actualBooks.get(1).getTitle());
    }
}
