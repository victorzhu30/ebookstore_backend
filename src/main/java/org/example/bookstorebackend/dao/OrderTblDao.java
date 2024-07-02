package org.example.bookstorebackend.dao;

import org.aspectj.weaver.ast.Or;
import org.example.bookstorebackend.entity.OrderTbl;

import java.time.Instant;
import java.util.List;

public interface OrderTblDao {
    OrderTbl findOne(Long id);
    List<OrderTbl> findAll();

    List<OrderTbl> findByCreatedAtBetween(Instant start, Instant end);
}
