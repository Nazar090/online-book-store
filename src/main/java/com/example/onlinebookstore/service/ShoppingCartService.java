package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.cartitem.CartItemQuantityDto;
import com.example.onlinebookstore.dto.cartitem.CartItemRequestDto;
import com.example.onlinebookstore.dto.cartitem.CartItemResponseDto;
import com.example.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCartByUserEmail(String email);

    CartItemResponseDto addCartItem(String email, CartItemRequestDto cartItemRequestDto);

    CartItemResponseDto updateBookQuantity(Long cartItemId,
                                           CartItemQuantityDto cartItemQuantityDto);

    void deleteCartItemById(Long cartItemId);
}
