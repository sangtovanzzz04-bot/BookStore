package org.example;

import org.example.model.Cart;
import org.example.model.Cartitem;
import org.example.repository.CartRepository;
import org.example.service.BookService;
import org.example.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class CartServiceTest {
    private CartService cartService;
    private BookService bookService;
    private CartRepository cartRepository;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void check_create_cart_if_not_exist() {
        Long userId = 1L;

        cartRepository = mock(CartRepository.class);
        when(cartRepository.getCartByUserId(userId)).thenReturn(Optional.empty());
        cartService = new CartService(cartRepository, bookService);
        Cart cart = cartService.createCart(userId);
        verify(cartRepository, times(1)).saveCart(cart);
    }

    @Test
    public void check_create_cart_if_exist() {
        Long userId = 1L;
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUserId(userId);
        cart.setItems(new ArrayList<>());
        cartRepository = mock(CartRepository.class);
        when(cartRepository.getCartByUserId(userId)).thenReturn(Optional.of(cart));
        cartService = new CartService(cartRepository, bookService);
        verify(cartRepository, never()).saveCart(cart);
    }

    @Test
    public void check_add_book_to_cart_with_request_quantity_lower_than_quantity() {
        Long userId = 1L;
        Long bookId = 1L;
        Integer requestQuantity = 2;

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUserId(userId);
        cart.setItems(new ArrayList<>());
        cartRepository = mock(CartRepository.class);
        bookService = mock(BookService.class);
        when(cartRepository.getCartByUserId(userId)).thenReturn(Optional.of(cart));
        when(bookService.checkStockAvailable(bookId, requestQuantity)).thenReturn(true);
        cartService = new CartService(cartRepository, bookService);
        Cartitem cartitem = cartService.addBookToCart(userId, bookId, requestQuantity);
        assertThat(cartitem.getQuantity(), equalTo(requestQuantity));
        verify(cartRepository, times(1)).saveCart(cart);
    }

    @Test
    public void check_add_book_to_cart_with_request_quantity_greater_than_quantity() {
        Long userId = 1L;
        Long bookId = 1L;
        Integer requestQuantity = 15;
        Integer quantityAvailable = 10;

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUserId(userId);
        cart.setItems(new ArrayList<>());
        cartRepository = mock(CartRepository.class);
        bookService = mock(BookService.class);
        when(cartRepository.getCartByUserId(userId)).thenReturn(Optional.of(cart));
        when(bookService.checkStockAvailable(bookId, requestQuantity)).thenReturn(false);
        when(bookService.getBookQuantity(bookId)).thenReturn(quantityAvailable);
        cartService = new CartService(cartRepository, bookService);
        Cartitem cartitem = cartService.addBookToCart(userId, bookId, requestQuantity);

        assertThat(cartitem.getQuantity(), equalTo(quantityAvailable));
        verify(cartRepository, times(1)).saveCart(cart);
    }
}
