package org.example.bookstorebackend.repository;

import org.example.bookstorebackend.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findUserById(Long id);

//    List<User> findAll();
}