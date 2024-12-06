package com.example.onlinebookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinebookstore.dto.book.BookDto;
import com.example.onlinebookstore.dto.book.CreateBookRequestDto;
import com.example.onlinebookstore.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

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
    @Sql(scripts = "classpath:database/books/add-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/cleanup-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void getAllBooks_ShouldReturnBooks() throws Exception {
        // Given
        List<BookDto> expectedBooks = List.of(
                createBook(1L, "Book 1", "Author 1", "12345"));

        // When
        var result = mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<BookDto> actualBooks = objectMapper.readValue(
                jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, BookDto.class)
        );

        List<BookDto> mutableActualBooks = new ArrayList<>(actualBooks);
        List<BookDto> mutableExpectedBooks = new ArrayList<>(expectedBooks);

        mutableActualBooks.sort(Comparator.comparing(BookDto::getId));
        mutableExpectedBooks.sort(Comparator.comparing(BookDto::getId));

        // Then
        Assertions.assertEquals(mutableExpectedBooks.size(), mutableActualBooks.size());
        Assertions.assertEquals(mutableExpectedBooks.get(0).getTitle(), mutableActualBooks.get(0).getTitle());
        Assertions.assertEquals(mutableExpectedBooks.get(0).getId(), mutableActualBooks.get(0).getId());

    }


    @Test
    @Sql(scripts = "classpath:database/books/add-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/cleanup-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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

        Assertions.assertNotNull(actualBook);
        Assertions.assertEquals(expectedTitle, actualBook.getTitle());
    }

    @Test
    @Sql(scripts = "classpath:database/books/add-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/cleanup-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(roles = "ADMIN")
    void createBook_ShouldSaveAndReturnBook() throws Exception {
        // Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("New Book")
                .setAuthor("New Author")
                .setIsbn("34567")
                .setPrice(BigDecimal.valueOf(10.00))
                .setDescription("New Description")
                .setCoverImage("New Cover")
                .setCategoryIds(Set.of(1L));

        // When
        var result = mockMvc.perform(post("/books")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        BookDto actualBook = objectMapper.readValue(jsonResponse, BookDto.class);

        Assertions.assertNotNull(actualBook);
        Assertions.assertEquals("New Book", actualBook.getTitle());
        Assertions.assertEquals("New Author", actualBook.getAuthor());
    }

    @Test
    @Sql(scripts = "classpath:database/books/add-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/cleanup-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(roles = "ADMIN")
    void updateBook_ShouldUpdateAndReturnBook() throws Exception {
        // Given
        Long bookId = 1L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Updated Book")
                .setAuthor("Updated Author")
                .setIsbn("67890")
                .setPrice(BigDecimal.valueOf(30.00))
                .setDescription("Updated Description")
                .setCoverImage("Updated Cover")
                .setCategoryIds(Set.of(1L));

        // When
        var result = mockMvc.perform(put("/books/{id}", bookId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = result.getResponse().getContentAsString();
        BookDto actualBook = objectMapper.readValue(jsonResponse, BookDto.class);

        Assertions.assertNotNull(actualBook);
        Assertions.assertEquals("Updated Book", actualBook.getTitle());
        Assertions.assertEquals("Updated Author", actualBook.getAuthor());
    }

    @Test
    @Sql(scripts = "classpath:database/books/add-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/cleanup-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(roles = "ADMIN")
    void deleteBook_ShouldReturnNoContent() throws Exception {
        // Given
        Long bookId = 1L;

        // When then
        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isNoContent());
    }

    private BookDto createBook(Long id, String title, String author, String isbn) {
        return new BookDto()
                .setId(id)
                .setTitle(title)
                .setAuthor(author)
                .setIsbn(isbn)
                .setPrice(BigDecimal.valueOf(20.00))
                .setDescription("Sample Description")
                .setCategoryIds(List.of(1L, 2L));
    }
}
