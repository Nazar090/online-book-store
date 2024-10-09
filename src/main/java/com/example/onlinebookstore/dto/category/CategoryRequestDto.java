package com.example.onlinebookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryRequestDto {
    @NotBlank
    private String name;
    private String description;
}
