package org.example.bookstorebackend.repository;

import org.example.bookstorebackend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}