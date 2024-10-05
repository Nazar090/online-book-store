package com.example.onlinebookstore.dto.cartitem;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItemRequestDto {
    private Long bookId;
    @Min(1)
    private int quantity;
}
