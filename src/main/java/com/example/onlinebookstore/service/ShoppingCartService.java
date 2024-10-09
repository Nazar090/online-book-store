package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.cartitem.CartItemQuantityDto;
import com.example.onlinebookstore.dto.cartitem.CartItemRequestDto;
import com.example.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.onlinebookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCartByUserId(Long userId);

    ShoppingCartDto addCartItem(Long userId, CartItemRequestDto cartItemRequestDto);

    ShoppingCartDto updateBookQuantity(Long userId, Long cartItemId,
                                       CartItemQuantityDto cartItemQuantityDto);

    void deleteCartItemById(Long cartItemId);

    void registerNewShoppingCart(User user);
}
