package org.example.bookstorebackend.service;

import org.example.bookstorebackend.entity.OrderTbl;

import java.time.Instant;
import java.util.List;

public interface OrderTblService {

    OrderTbl findOne(Long id);
    List<OrderTbl> findAll();

    List<OrderTbl> findByCreatedAtBetween(Instant start, Instant end);
}
