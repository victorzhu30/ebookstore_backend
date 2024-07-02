package org.example.bookstorebackend.daoImpl;

import org.example.bookstorebackend.dao.BookDao1;
import org.example.bookstorebackend.entity.Book;
import org.example.bookstorebackend.repository.BookRepository1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookDaoImpl1 implements BookDao1 {

    @Autowired
    private BookRepository1 bookRepository1;

    @Override
    public Book updateBook(Book book){
        return bookRepository1.save(book);
    }

    @Override
    public void deleteById(Long id){
        bookRepository1.deleteById(id);
    }
}
