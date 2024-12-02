package com.example.onlinebookstore.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinebookstore.dto.book.BookDto;
import com.example.onlinebookstore.dto.book.CreateBookRequestDto;
import com.example.onlinebookstore.security.JwtUtil;
import com.example.onlinebookstore.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
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
class BookControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

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
    @WithMockUser(roles = "USER")
    void getAll_ShouldReturnPaginatedBookList() throws Exception {
        List<BookDto> expectedBooks = List.of(
                new BookDto()
                        .setId(1L)
                        .setTitle("Book 1")
                        .setAuthor("Author 1")
                        .setIsbn("12345")
                        .setPrice(BigDecimal.valueOf(20.00))
                        .setDescription("Description 1")
                        .setCategoryIds(null),
                new BookDto()
                        .setId(2L)
                        .setTitle("Book 2")
                        .setAuthor("Author 2")
                        .setIsbn("67890")
                        .setPrice(BigDecimal.valueOf(25.00))
                        .setDescription("Description 2")
                        .setCategoryIds(null)
        );

        Mockito.when(bookService.findAll(any())).thenReturn(expectedBooks);

        MvcResult result = mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<BookDto> actualBooks = objectMapper.readValue(
                jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, BookDto.class)
        );

        Assertions.assertNotNull(actualBooks);
        Assertions.assertEquals(expectedBooks.size(), actualBooks.size());
        Assertions.assertEquals(expectedBooks.get(0).getId(), actualBooks.get(0).getId());
        Assertions.assertEquals(expectedBooks.get(1).getIsbn(), actualBooks.get(1).getIsbn());
        Assertions.assertEquals(expectedBooks.get(0).getTitle(), actualBooks.get(0).getTitle());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBookById_ShouldReturnBook() throws Exception {
        Long bookId = 1L;
        BookDto expectedBook = new BookDto()
                .setId(1L)
                .setTitle("Sample Book")
                .setAuthor("Sample Author")
                .setIsbn("12345")
                .setPrice(BigDecimal.valueOf(20.00))
                .setDescription("Sample Description")
                .setCategoryIds(List.of(1L, 2L));

        Mockito.when(bookService.findById(eq(bookId))).thenReturn(expectedBook);

        MvcResult result = mockMvc.perform(get("/books/{id}", bookId))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        BookDto actualBook = objectMapper.readValue(jsonResponse, BookDto.class);

        Assertions.assertNotNull(actualBook);
        Assertions.assertEquals(expectedBook.getId(), actualBook.getId());
        Assertions.assertEquals(expectedBook.getIsbn(), actualBook.getIsbn());
        Assertions.assertEquals(expectedBook.getTitle(), actualBook.getTitle());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBook_ShouldReturnCreatedBook() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Sample Book")
                .setAuthor("Sample Author")
                .setIsbn("12345")
                .setPrice(BigDecimal.valueOf(20.00))
                .setDescription("Sample Description")
                .setCoverImage("Cover Image")
                .setCategoryIds(Set.of(1L, 2L));

        BookDto expectedBook = new BookDto()
                .setId(1L)
                .setTitle("Sample Book")
                .setAuthor("Sample Author")
                .setIsbn("12345")
                .setPrice(BigDecimal.valueOf(20.00))
                .setDescription("Sample Description")
                .setCoverImage("Cover Image")
                .setCategoryIds(List.of(1L, 2L));

        Mockito.when(bookService.save(any())).thenReturn(expectedBook);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        BookDto actualBook = objectMapper.readValue(jsonResponse, BookDto.class);

        Assertions.assertNotNull(actualBook);
        Assertions.assertEquals(expectedBook.getId(), actualBook.getId());
        Assertions.assertEquals(expectedBook.getIsbn(), actualBook.getIsbn());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateBook_ShouldReturnUpdatedBook() throws Exception {
        Long bookId = 1L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Updated Book")
                .setAuthor("Updated Author")
                .setIsbn("67890")
                .setPrice(BigDecimal.valueOf(30.00))
                .setDescription("Updated Description")
                .setCoverImage("Updated Cover")
                .setCategoryIds(Set.of(3L));

        BookDto updatedBook = new BookDto()
                .setId(1L)
                .setTitle("Updated Book")
                .setAuthor("Updated Author")
                .setIsbn("67890")
                .setPrice(BigDecimal.valueOf(30.00))
                .setDescription("Updated Description")
                .setCoverImage("Updated Cover")
                .setCategoryIds(List.of(3L));

        Mockito.when(bookService.updateBook(eq(bookId), any())).thenReturn(updatedBook);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/books/{id}", bookId)
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        BookDto actualBook = objectMapper.readValue(jsonResponse, BookDto.class);

        Assertions.assertNotNull(actualBook);
        Assertions.assertEquals(updatedBook.getId(), actualBook.getId());
        Assertions.assertEquals(updatedBook.getIsbn(), actualBook.getIsbn());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteBook_ShouldReturnNoContent() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isNoContent());
        
        Mockito.verify(bookService).deleteById(bookId);
    }
}
