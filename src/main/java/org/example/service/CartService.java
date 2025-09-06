package org.example.service;

import org.example.model.Cart;
import org.example.model.Cartitem;
import org.example.repository.CartRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartService {
    private CartRepository cartRepository;
    private BookService bookService;
    public CartService(CartRepository cartRepository, BookService bookService) {
        this.cartRepository = cartRepository;
        this.bookService = bookService;
    }

    public Cart createCart(Long userId) {
        Optional<Cart> findCart = cartRepository.getCartByUserId(userId);
        if (findCart.isEmpty()) {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setItems(new ArrayList<>());
            cartRepository.saveCart(cart);
            return cart;
        }
        return findCart.get();
    }

    public Cartitem addBookToCart(Long userId, Long bookId, Integer quantity) {
        Cart cart = createCart(userId);
        List<Cartitem> cartitems = cart.getItems();
        if(bookService.checkStockAvailable(bookId, quantity)) {

            Cartitem cartitem = new Cartitem();
            cartitem.setBookId(bookId);
            cartitem.setQuantity(quantity);

            cartitems.add(cartitem);
            cart.setItems(cartitems);
            cartRepository.saveCart(cart);
            return cartitem;
        } else {
            Integer quantityAvailable = bookService.getBookQuantity(bookId);

            Cartitem cartitem = new Cartitem();
            cartitem.setBookId(bookId);
            cartitem.setQuantity(quantityAvailable);

            cartitems.add(cartitem);
            cart.setItems(cartitems);
            cartRepository.saveCart(cart);
            return cartitem;
        }
    }

    public void removeBookFromCart(Long userId, Long bookId, Integer quantity) {
    }

    public void clearCart(Long userId) {
    }
}
