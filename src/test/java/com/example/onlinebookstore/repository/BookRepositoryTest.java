package com.example.onlinebookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.onlinebookstore.config.CustomMySqlContainer;
import com.example.onlinebookstore.model.Book;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class BookRepositoryTest {

    private static final CustomMySqlContainer mysqlContainer = CustomMySqlContainer.getInstance();

    @Autowired
    private BookRepository bookRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    @Test
    @Sql(scripts = "classpath:database/books/setup-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/cleanup-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldFindBooksByCategoryId() {
        Long categoryId = 1L;
        List<Book> books = bookRepository.findAllByCategoryId(categoryId);

        assertNotNull(books, "The result should not be null");
        assertEquals(1, books.size(), "There should be one book in the specified category");
        assertEquals("The Great Gatsby", books.get(0).getTitle(), "The book title should match");
    }
}