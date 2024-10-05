package com.example.onlinebookstore.controller;

import com.example.onlinebookstore.dto.cartitem.CartItemQuantityDto;
import com.example.onlinebookstore.dto.cartitem.CartItemRequestDto;
import com.example.onlinebookstore.dto.cartitem.CartItemResponseDto;
import com.example.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.onlinebookstore.exception.EntityNotFoundException;
import com.example.onlinebookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
@Tag(name = "Shopping cart", description = "Operations related to shopping cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a cart item", description = "Add a cart item to shopping cart")
    public CartItemResponseDto addCartItem(
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        return shoppingCartService.addCartItem(getAuthenticationName(), cartItemRequestDto);
    }

    @GetMapping
    @Operation(summary = "Get a shopping cart", description = "Get a shopping cart")
    public ShoppingCartResponseDto getShoppingCart() {
        return shoppingCartService.getShoppingCartByUserEmail(getAuthenticationName());
    }

    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update a quantity", description = "Update a quantity book")
    public CartItemResponseDto updateCartItem(@PathVariable Long cartItemId,
                                      @RequestBody @Valid CartItemQuantityDto cartItemQuantityDto) {
        return shoppingCartService.updateBookQuantity(cartItemId, cartItemQuantityDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Delete a cart item", description = "Delete a cart item id")
    public void deleteCartItem(@PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItemById(cartItemId);
    }

    private String getAuthenticationName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        throw new EntityNotFoundException(
                "Can't find authentication name by authentication " + authentication);
    }
}