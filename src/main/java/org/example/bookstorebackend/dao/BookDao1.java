package org.example.bookstorebackend.dao;

import org.example.bookstorebackend.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookDao1 {
    Book updateBook(Book book);
    void deleteById(Long id);
}
