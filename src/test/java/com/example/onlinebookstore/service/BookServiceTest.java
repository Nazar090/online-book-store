package com.example.onlinebookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.onlinebookstore.dto.book.BookDto;
import com.example.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.onlinebookstore.dto.book.CreateBookRequestDto;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.mapper.BookMapper;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.Category;
import com.example.onlinebookstore.repository.BookRepository;
import com.example.onlinebookstore.repository.CategoryRepository;
import com.example.onlinebookstore.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDto bookDto;
    private CreateBookRequestDto createBookRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Book();
        book.setId(1L);
        book.setTitle("Sample Book");

        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Sample Book");

        createBookRequestDto = new CreateBookRequestDto()
                .setTitle("Sample Book")
                .setAuthor("John Doe")
                .setIsbn("978-3-16-148410-0")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("A fascinating book about the wonders of the world.")
                .setCoverImage("http://example.com/sample-cover.jpg")
                .setCategoryIds(Set.of(1L, 2L));
    }

    @Test
    void testSave_Success() {
        when(bookMapper.toBookEntity(createBookRequestDto)).thenReturn(book);
        when(categoryRepository.findAllById(createBookRequestDto.getCategoryIds()))
                .thenReturn(List.of(new Category().setId(1L), new Category().setId(2L)));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.save(createBookRequestDto);

        assertEquals(bookDto, result);
        verify(bookMapper).toBookEntity(createBookRequestDto);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @Test
    void testFindAll_Success() {
        Page<Book> mockPage = new PageImpl<>(List.of(book));
        when(bookRepository.findAll(Pageable.unpaged())).thenReturn(mockPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> result = bookService.findAll(Pageable.unpaged());

        assertEquals(1, result.size());
        assertEquals(bookDto, result.get(0));
        verify(bookRepository).findAll(Pageable.unpaged());
        verify(bookMapper).toDto(book);
    }

    @Test
    void testFindById_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.findById(1L);

        assertEquals(bookDto, result);
        verify(bookRepository).findById(1L);
        verify(bookMapper).toDto(book);
    }

    @Test
    void testFindById_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.findById(1L));
        verify(bookRepository).findById(1L);
    }

    @Test
    void testUpdateBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.updateBook(1L, createBookRequestDto);

        assertEquals(bookDto, result);
        verify(bookRepository).findById(1L);
        verify(bookMapper).updateBookFromDto(createBookRequestDto, book);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @Test
    void testUpdateBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookService.updateBook(1L, createBookRequestDto));
        verify(bookRepository).findById(1L);
    }

    @Test
    void testDeleteById_Success() {
        bookService.deleteById(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void testFindAllByCategoryId_Success() {
        when(bookRepository.findAllByCategoryId(1L)).thenReturn(List.of(book));
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds.setId(1L);
        bookDtoWithoutCategoryIds.setTitle("Sample Book");
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> result = bookService.findAllByCategoryId(1L);

        assertEquals(1, result.size());
        assertEquals(bookDtoWithoutCategoryIds, result.get(0));
        verify(bookRepository).findAllByCategoryId(1L);
        verify(bookMapper).toDtoWithoutCategories(book);
    }
}
