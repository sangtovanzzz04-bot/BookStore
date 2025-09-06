package org.example.service;

import org.example.model.Cartitem;
import org.example.model.Order;
import org.example.repository.CartitemRepository;
import org.example.repository.OrderRepository;

public class OrderService {
    private OrderRepository orderRepository;
    private CartitemRepository cartitemRepository;
    private BookService bookService;

    public OrderService(CartitemRepository cartitemRepository, BookService bookService, OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.cartitemRepository = cartitemRepository;
        this.bookService = bookService;
    }

    public Order checkout(Long cartitemId) {
        Cartitem cartitem = cartitemRepository.findByCartItemId(cartitemId);
        if(bookService.checkStockAvailable(cartitem.getBookId(), cartitem.getQuantity())) {
            bookService.updateStockQuantity(cartitem.getBookId(), cartitem.getQuantity());
            Order order = new Order();
            order.setBookId(cartitem.getBookId());
            order.setQuantity(cartitem.getQuantity());
            orderRepository.saveOrder(order);
            return order;
        } else {
            Integer quantity = bookService.getBookQuantity(cartitem.getBookId());
            bookService.updateStockQuantity(cartitem.getBookId(), quantity);
            Order order = new Order();
            order.setBookId(cartitem.getBookId());
            order.setQuantity(quantity);
            orderRepository.saveOrder(order);
            return order;
        }
    }
}
