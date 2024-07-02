package org.example.bookstorebackend.repository;

import org.example.bookstorebackend.entity.OrderTbl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderTblRepository extends JpaRepository<OrderTbl, Long> {
    List<OrderTbl> findByCreatedAtBetween(Instant start, Instant end);
}