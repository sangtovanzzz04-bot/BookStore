package org.example.repository;

import org.example.model.Cart;

import java.util.Optional;

public class CartRepository {
    public Optional<Cart> getCartByUserId(Long userId) {
        return Optional.empty();
    }

    public void saveCart(Cart cart) {

    }
}
