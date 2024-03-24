package com.rouvsen.demo.dao;

import com.rouvsen.demo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionDAO extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByHasCashback(boolean hasCashback);
}
