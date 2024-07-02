package org.example.bookstorebackend.daoImpl;

import org.example.bookstorebackend.dao.UserDao;
import org.example.bookstorebackend.entity.User;
import org.example.bookstorebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(Long id) {
        return userRepository.findUserById(id);
    }
}
