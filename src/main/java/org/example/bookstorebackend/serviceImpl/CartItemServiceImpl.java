package org.example.bookstorebackend.serviceImpl;

import org.example.bookstorebackend.dao.CartItemDao;
import org.example.bookstorebackend.entity.CartItemDto;
import org.example.bookstorebackend.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {
    @Autowired
    private CartItemDao cartItemDao;

    @Override
    public List<CartItemDto> findAll() {
        return cartItemDao.getAllCartItems();
    }
}
