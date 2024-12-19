package com.example.onlinebookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinebookstore.dto.book.BookDto;
import com.example.onlinebookstore.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
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
class BookControllerTest {
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
    @WithMockUser(roles = "USER")
    void getAllBooks_ShouldReturnBooks() throws Exception {
        // Given
        List<BookDto> expectedBooks = List.of(
                createBookDto(1L, "Book 1", "Author 1", "12345"),
                createBookDto(2L, "Book 2", "Author 2", "67890"));

        // When
        var result = mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<BookDto> actualBooks = objectMapper.readValue(
                jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, BookDto.class)
        );

        // Then
        assertEquals(expectedBooks.size(), actualBooks.size());
        assertEquals(expectedBooks.get(0).getTitle(),
                actualBooks.get(0).getTitle());
        assertEquals(expectedBooks.get(0).getId(),
                actualBooks.get(0).getId());

    }

    @Test
    @WithMockUser(roles = "USER")
    void getBookById_ShouldReturnBook() throws Exception {
        // Given
        Long bookId = 1L;
        String expectedTitle = "Book 1";

        // When
        var result = mockMvc.perform(get("/books/{id}", bookId))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        BookDto actualBook = objectMapper.readValue(jsonResponse, BookDto.class);

        assertNotNull(actualBook);
        assertEquals(expectedTitle, actualBook.getTitle());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBook_ShouldSaveAndReturnBook() throws Exception {
        // Given
        CreateBookRequestDto requestDto = createBookRequestDto(
                "New Book",
                "New Author",
                "34567");

        // When
        var result = mockMvc.perform(post("/books")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        BookDto actualBook = objectMapper.readValue(jsonResponse, BookDto.class);

        assertNotNull(actualBook);
        assertEquals("New Book", actualBook.getTitle());
        assertEquals("New Author", actualBook.getAuthor());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateBook_ShouldUpdateAndReturnBook() throws Exception {
        // Given
        Long bookId = 1L;
        CreateBookRequestDto requestDto = createBookRequestDto(
                "Updated Book",
                "Updated Author",
                "12345");

        // When
        var result = mockMvc.perform(put("/books/{id}", bookId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        BookDto actualBook = objectMapper.readValue(jsonResponse, BookDto.class);

        assertNotNull(actualBook);
        assertEquals("Updated Book", actualBook.getTitle());
        assertEquals("Updated Author", actualBook.getAuthor());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteBook_ShouldReturnNoContent() throws Exception {
        // Given
        Long bookId = 1L;

        // When then
        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isNoContent());
    }

    private BookDto createBookDto(Long id, String name, String author, String isbn) {
        return new BookDto()
                .setId(id)
                .setTitle(name)
                .setAuthor(author)
                .setIsbn(isbn)
                .setPrice(BigDecimal.valueOf(20.00))
                .setDescription("Sample Description")
                .setCategoryIds(List.of(1L, 2L));
    }

    private CreateBookRequestDto createBookRequestDto(String name, String author, String isbn) {
        return new CreateBookRequestDto()
                .setTitle(name)
                .setAuthor(author)
                .setIsbn(isbn)
                .setPrice(BigDecimal.valueOf(20.00))
                .setDescription("Sample Description")
                .setCategoryIds(Set.of(1L, 2L));
    }
}
