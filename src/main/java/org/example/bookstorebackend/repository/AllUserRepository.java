package org.example.bookstorebackend.repository;

import org.example.bookstorebackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllUserRepository extends JpaRepository<User, Long> {
}