package org.example.bookstorebackend.daoImpl;

import org.example.bookstorebackend.dao.BookDao;
import org.example.bookstorebackend.entity.Book;
import org.example.bookstorebackend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookDaoImpl implements BookDao {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findBookById(id);
    }

    @Override
    public Page<Book> findAll(Pageable bookPage) {
        return bookRepository.findAll(bookPage);
    }

    @Override
    public Page<Book> findAllByTitle(String title, Pageable bookPage) {
        return bookRepository.findAllByTitleContaining(title, bookPage);
    }

    @Override
    public List<Book> getTop10BestSellingBooks() {
        return bookRepository.findTop10BestSellingBooks();
    }
}
