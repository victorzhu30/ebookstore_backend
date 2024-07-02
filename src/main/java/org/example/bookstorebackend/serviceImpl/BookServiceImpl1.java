package org.example.bookstorebackend.serviceImpl;

import org.example.bookstorebackend.dao.BookDao1;
import org.example.bookstorebackend.entity.Book;
import org.example.bookstorebackend.service.BookService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl1 implements BookService1 {
    @Autowired
    private BookDao1 bookDao1;

    @Override
    public Book updateBook(Book book) {
        return bookDao1.updateBook(book);
    }

    public void deleteById(Long id) {
        bookDao1.deleteById(id);
    }
}
