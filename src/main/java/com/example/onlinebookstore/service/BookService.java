package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.BookDto;
import com.example.onlinebookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    void save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    void updateBook(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);
}
