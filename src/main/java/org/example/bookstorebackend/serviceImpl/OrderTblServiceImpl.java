package org.example.bookstorebackend.serviceImpl;

import org.example.bookstorebackend.dao.OrderTblDao;
import org.example.bookstorebackend.entity.OrderTbl;
import org.example.bookstorebackend.service.OrderTblService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class OrderTblServiceImpl implements OrderTblService {
    @Autowired
    private OrderTblDao orderTblDao;

    @Override
    public OrderTbl findOne(Long id) {
        return orderTblDao.findOne(id);
    }

    @Override
    public List<OrderTbl> findAll() {
        return orderTblDao.findAll();
    }

    @Override
    public List<OrderTbl> findByCreatedAtBetween(Instant start, Instant end) {
        return orderTblDao.findByCreatedAtBetween(start, end);
    }


}
