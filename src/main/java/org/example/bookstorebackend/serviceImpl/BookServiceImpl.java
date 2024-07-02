package org.example.bookstorebackend.serviceImpl;

import org.example.bookstorebackend.dao.BookDao;
import org.example.bookstorebackend.entity.Book;
import org.example.bookstorebackend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookDao bookDao;

    @Override
    public Book findBookById(Long id) {
        return bookDao.getBookById(id);
    }

    @Override
    public Page<Book> findAll(Pageable bookPage) {
        return bookDao.findAll(bookPage);
    }

    @Override
    public Page<Book> findAllByTitleContaining(String title, Pageable bookPage) {
        return bookDao.findAllByTitle(title, bookPage);
    }

    @Override
    public List<Book> getTop10BestSellingBooks() {
        return bookDao.getTop10BestSellingBooks();
    }
}
