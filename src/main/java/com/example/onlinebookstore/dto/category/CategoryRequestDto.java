package com.example.onlinebookstore.dto.category;

import jakarta.validation.constraints.NotBlank;

public class CategoryRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}