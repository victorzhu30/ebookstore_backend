package org.example.bookstorebackend.service;

import org.example.bookstorebackend.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService1 {

    Book updateBook(Book book);

    void deleteById(Long id);
}
