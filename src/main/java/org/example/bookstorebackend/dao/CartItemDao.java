package org.example.bookstorebackend.dao;

import org.example.bookstorebackend.entity.CartItem;
import org.example.bookstorebackend.entity.CartItemDto;

import java.util.List;

public interface CartItemDao {

    CartItemDto convertToDto(CartItem cartItem);
    List<CartItemDto> getAllCartItems();
}
