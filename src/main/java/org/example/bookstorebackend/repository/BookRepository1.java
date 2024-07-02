package org.example.bookstorebackend.repository;

import org.example.bookstorebackend.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository1 extends JpaRepository<Book, Long> {

    void deleteById(Long id);
}