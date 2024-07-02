package org.example.bookstorebackend.dao;

import org.example.bookstorebackend.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookDao {
    Book getBookById(Long id);
    Page<Book> findAll(Pageable bookPage);
    Page<Book> findAllByTitle(String title, Pageable bookPage);

    List<Book> getTop10BestSellingBooks();
}
