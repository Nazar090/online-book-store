package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.book.BookDto;
import com.example.onlinebookstore.dto.book.CreateBookRequestDto;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.mapper.BookMapper;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public void save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toBookEntity(requestDto);
        bookRepository.save(book);
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Book not found by id: " + id));

    }

    @Override
    public void updateBook(Long id, CreateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Book not found"));
        bookMapper.updateBookFromDto(requestDto, book);
        bookRepository.save(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.softDeleteById(id);
    }
}
