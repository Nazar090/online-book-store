package com.example.onlinebookstore.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookDto {
    private Long id;
    @NotBlank(message = "Title mustn't be null or empty")
    private String title;
    @NotBlank(message = "Author mustn't be null or empty")
    private String author;
    @NotBlank(message = "Isbn mustn't be null or empty")
    private String isbn;
    @Min(value = 0, message = "Price mustn't be negative")
    private BigDecimal price;
    private String description;
    private String coverImage;
    private List<Long> categoryIds;
}
