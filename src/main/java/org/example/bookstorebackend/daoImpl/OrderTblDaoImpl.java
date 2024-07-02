package org.example.bookstorebackend.daoImpl;

import org.example.bookstorebackend.dao.OrderTblDao;
import org.example.bookstorebackend.entity.OrderTbl;
import org.example.bookstorebackend.repository.OrderTblRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public class OrderTblDaoImpl implements OrderTblDao {
    @Autowired
    private OrderTblRepository orderTblRepository;

    @Override
    public OrderTbl findOne(Long id) {
        return orderTblRepository.getOne(id);
    }

    @Override
    public List<OrderTbl> findAll() {
        return orderTblRepository.findAll();
    }

    @Override
    public List<OrderTbl> findByCreatedAtBetween(Instant start, Instant end) {
        return orderTblRepository.findByCreatedAtBetween(start, end);
    }
}
