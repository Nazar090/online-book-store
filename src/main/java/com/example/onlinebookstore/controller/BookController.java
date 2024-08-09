package com.example.onlinebookstore.controller;

import com.example.onlinebookstore.dto.BookDto;
import com.example.onlinebookstore.dto.CreateBookRequestDto;
import com.example.onlinebookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
@Tag(name = "Books", description = "Operations related to books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books",
            description = "Retrieve a list of all books with pagination and sorting options")
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("{id}")
    @ResponseBody
    @Operation(summary = "Get a book by ID", description = "Retrieve a specific book by its ID")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new book", description = "Add a new book to the store")
    public void createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        bookService.save(bookDto);
    }

    @PutMapping("{id}")
    @Operation(summary = "Put a book by ID", description = "Put a new book by a certain ID")
    public void updateBook(@PathVariable Long id,
                           @RequestBody @Valid CreateBookRequestDto bookDto) {
        bookService.updateBook(id, bookDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a book by id", description = "Delete a specific book by its ID")
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }
}
