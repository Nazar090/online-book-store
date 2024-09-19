package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.book.BookDto;
import com.example.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.onlinebookstore.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    void save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void updateBook(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId);
}
