package com.example.onlinebookstore.mapper;

import com.example.onlinebookstore.dto.BookDto;
import com.example.onlinebookstore.dto.CreateBookRequestDto;
import com.example.onlinebookstore.model.Book;

public interface BookMapper {
    BookDto toDto(Book book);

    Book toBookEntity(CreateBookRequestDto requestDto);
}
