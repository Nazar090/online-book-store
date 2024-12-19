package com.example.onlinebookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.onlinebookstore.dto.category.CategoryDto;
import com.example.onlinebookstore.dto.category.CategoryRequestDto;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.mapper.CategoryMapper;
import com.example.onlinebookstore.model.Category;
import com.example.onlinebookstore.repository.CategoryRepository;
import com.example.onlinebookstore.service.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDto categoryDto;
    private CategoryRequestDto categoryRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category();
        category.setId(1L);
        category.setName("Fiction");

        categoryDto = new CategoryDto(1L, "Fiction", "description");

        categoryRequestDto = new CategoryRequestDto()
                .setName("Fiction")
                .setDescription("description");
    }

    @Test
    void testFindAll_Success() {
        Page<Category> mockPage = new PageImpl<>(List.of(category));
        when(categoryRepository.findAll(Pageable.unpaged())).thenReturn(mockPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        List<CategoryDto> result = categoryService.findAll(Pageable.unpaged());

        assertEquals(List.of(categoryDto), result);
        verify(categoryRepository).findAll(Pageable.unpaged());
        verify(categoryMapper).toDto(category);
    }

    @Test
    void testGetById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(1L);

        assertEquals(categoryDto, result);
        verify(categoryRepository).findById(1L);
        verify(categoryMapper).toDto(category);
    }

    @Test
    void testGetById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getById(1L);
        });

        assertEquals("Category not found by id: 1", exception.getMessage());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void testSave_Success() {
        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.save(categoryRequestDto);

        assertEquals(categoryDto, result);
        verify(categoryMapper).toEntity(categoryRequestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    void testUpdate_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.update(1L, categoryRequestDto);

        assertEquals(categoryDto, result);
        verify(categoryRepository).findById(1L);
        verify(categoryMapper).updateCategoryFromDto(categoryRequestDto, category);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    void testUpdate_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.update(1L, categoryRequestDto);
        });

        assertEquals("Can't find category by id 1", exception.getMessage());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void testDeleteById() {
        categoryService.deleteById(1L);
        verify(categoryRepository).deleteById(1L);
    }
}
