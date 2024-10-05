package com.example.onlinebookstore.dto.cartitem;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CartItemQuantityDto {
    @Min(1)
    private int quantity;
}
