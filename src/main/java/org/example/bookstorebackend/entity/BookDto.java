package org.example.bookstorebackend.entity;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Book}
 */
@Value
public class BookDto implements Serializable {
    Long id;
    String cover;
    Integer price;
    String title;
    String description;
}