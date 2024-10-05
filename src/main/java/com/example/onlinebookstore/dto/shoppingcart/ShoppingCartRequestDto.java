package com.example.onlinebookstore.dto.shoppingcart;

import com.example.onlinebookstore.model.CartItem;
import com.example.onlinebookstore.model.User;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingCartRequestDto {
    private User user;
    private Set<CartItem> cartItems = new HashSet<>();
}
