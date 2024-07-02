package org.example.bookstorebackend.service;

import org.example.bookstorebackend.entity.CartItemDto;

import java.util.List;

public interface CartItemService {
    List<CartItemDto> findAll();
}
