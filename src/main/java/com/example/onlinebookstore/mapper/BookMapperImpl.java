package com.example.onlinebookstore.mapper;

import com.example.onlinebookstore.dto.BookDto;
import com.example.onlinebookstore.dto.CreateBookRequestDto;
import com.example.onlinebookstore.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapperImpl implements BookMapper {
    @Override
    public BookDto toDto(Book book) {
        return new BookDto(
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPrice(),
                book.getDescription(),
                book.getCoverImage()
        );
    }

    public Book toBookEntity(CreateBookRequestDto requestDto) {
        return new Book(
                requestDto.getTitle(),
                requestDto.getAuthor(),
                requestDto.getIsbn(),
                requestDto.getPrice(),
                requestDto.getDescription(),
                requestDto.getCoverImage()
        );
    }
}
