package org.example.service;

import org.example.model.Book;
import org.example.repository.BookRepository;

public class BookService {
    private BookRepository bookRepository;
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book getBookById(Long id) {
        return null;
    }

    public boolean checkStockAvailable(Long bookId, Integer amount) {
        return false;
    }

    public void updateStockQuantity(Long bookId, Integer amount) {
    }

    public Integer getBookQuantity(Long bookId) {
        Book book = getBookById(bookId);
        return book.getStockQuantity();
    }
}
