package org.example.bookstorebackend.serviceImpl;

import org.example.bookstorebackend.dao.UserDao;
import org.example.bookstorebackend.entity.User;
import org.example.bookstorebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }
}
