package com.example.onlinebookstore.dto.cartitem;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemQuantityDto {
    @Positive
    private int quantity;
}
