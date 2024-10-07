package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.cartitem.CartItemQuantityDto;
import com.example.onlinebookstore.dto.cartitem.CartItemRequestDto;
import com.example.onlinebookstore.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCartByUserId(Long userId);

    ShoppingCartDto addCartItem(Long userId, CartItemRequestDto cartItemRequestDto);

    ShoppingCartDto updateBookQuantity(Long cartItemId,
                                       CartItemQuantityDto cartItemQuantityDto);

    void deleteCartItemById(Long cartItemId);
}
