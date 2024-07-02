package org.example.bookstorebackend.service;

import org.example.bookstorebackend.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {

    Book findBookById(Long id);
    Page<Book> findAll(Pageable bookPage);
    Page<Book> findAllByTitleContaining(String title, Pageable bookPage);
    List<Book> getTop10BestSellingBooks();
}
