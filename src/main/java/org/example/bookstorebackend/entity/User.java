package org.example.bookstorebackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "balance")
    private Long balance;

    @Column(name = "email")
    private String email;

    @Column(name = "notes")
    private String notes;

    @Column(name = "is_banned")
    private Boolean isBanned;

    @Column(name = "is_admin")
    private Boolean isAdmin;
}