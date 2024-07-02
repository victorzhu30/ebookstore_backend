package org.example.bookstorebackend.dao;

import org.example.bookstorebackend.entity.User;

import java.util.List;

public interface UserDao {
    User getUserById(Long id);
}
