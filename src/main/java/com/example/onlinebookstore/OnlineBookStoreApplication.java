package com.example.onlinebookstore;

import com.example.onlinebookstore.dto.CreateBookRequestDto;
import com.example.onlinebookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineBookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            bookService.save(new CreateBookRequestDto(
                    "Sample Book 1",
                    "Author A",
                    "9781234567897",
                    new BigDecimal("19.99"),
                    "This is a sample book description.",
                    "http://example.com/cover1.jpg"
            ));

            bookService.save(new CreateBookRequestDto(
                    "Sample Book 2",
                    "Author B",
                    "9789876543210",
                    new BigDecimal("24.99"),
                    "Another sample book description.",
                    "http://example.com/cover2.jpg"
            ));
        };
    }
}
