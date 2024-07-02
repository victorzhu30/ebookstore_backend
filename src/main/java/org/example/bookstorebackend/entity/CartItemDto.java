package org.example.bookstorebackend.entity;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link CartItem}
 */
@Value
public class CartItemDto implements Serializable {
    Long id;
    Integer number;
    BookDto book;
    UserDto user;
}