package org.example;

import org.example.model.Cartitem;
import org.example.model.Order;
import org.example.repository.CartRepository;
import org.example.repository.CartitemRepository;
import org.example.repository.OrderRepository;
import org.example.service.BookService;
import org.example.service.OrderService;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class OrderServiceTest {
    private BookService bookService;
    private OrderService orderService;
    private CartitemRepository cartitemRepository;
    private OrderRepository orderRepository;

    @Test
    public void should_create_order_success_if_quantity_request_less_than_quantity() {
        Long bookId = 1L;
        Long cartitemId = 1L;
        Integer quantity_request = 1;
        Cartitem cartitem = new Cartitem();
        cartitem.setId(cartitemId);
        cartitem.setBookId(bookId);
        cartitem.setQuantity(quantity_request);


        cartitemRepository = mock(CartitemRepository.class);
        when(cartitemRepository.findByCartItemId(cartitemId)).thenReturn(cartitem);
        bookService = mock(BookService.class);
        when(bookService.checkStockAvailable(bookId, quantity_request)).thenReturn(true);
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderService(cartitemRepository, bookService, orderRepository);

        Order order = orderService.checkout(cartitemId);
        verify(orderRepository, times(1)).saveOrder(order);
    }

    @Test
    public void should_create_order_success_if_quantity_request_more_than_quantity() {
        Long bookId = 1L;
        Long cartitemId = 1L;
        Integer quantity_request = 15;
        Integer quantity_available = 10;
        Cartitem cartitem = new Cartitem();
        cartitem.setId(cartitemId);
        cartitem.setBookId(bookId);
        cartitem.setQuantity(quantity_request);


        cartitemRepository = mock(CartitemRepository.class);
        when(cartitemRepository.findByCartItemId(cartitemId)).thenReturn(cartitem);
        bookService = mock(BookService.class);
        when(bookService.checkStockAvailable(bookId, quantity_request)).thenReturn(false);
        when(bookService.getBookQuantity(bookId)).thenReturn(quantity_available);
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderService(cartitemRepository, bookService, orderRepository);

        Order order = orderService.checkout(cartitemId);
        verify(orderRepository, times(1)).saveOrder(order);
        assertThat(order.getQuantity(), equalTo(quantity_available));
    }

    @Test
    public void should_update_quantity_if_order_success() {
        Long bookId = 1L;
        Long cartitemId = 1L;
        Integer quantity_request = 15;
        Integer quantity_available = 10;
        Cartitem cartitem = new Cartitem();
        cartitem.setId(cartitemId);
        cartitem.setBookId(bookId);
        cartitem.setQuantity(quantity_request);


        cartitemRepository = mock(CartitemRepository.class);
        when(cartitemRepository.findByCartItemId(cartitemId)).thenReturn(cartitem);
        bookService = mock(BookService.class);
        when(bookService.checkStockAvailable(bookId, quantity_request)).thenReturn(true);
        when(bookService.getBookQuantity(bookId)).thenReturn(quantity_available);
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderService(cartitemRepository, bookService, orderRepository);

        Order order = orderService.checkout(cartitemId);
        verify(bookService, times(1)).updateStockQuantity(bookId, quantity_request);
    }
}
