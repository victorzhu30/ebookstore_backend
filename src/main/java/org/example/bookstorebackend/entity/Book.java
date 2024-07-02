package org.example.bookstorebackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "author")
    private String author;

    @Column(name = "cover")
    private String cover;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;//decimal or 分 / 100

    @Column(name = "title")
    private String title;

    @Column(name = "sales")
    private Integer sales;

    @Column(name ="stock")
    private Integer stock;

    @Column(name = "isbn")
    private String isbn;
}