package com.calculator.persistance.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Month;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findTransactionByNameAndValueAndMonth(String name, float value, Month month);
    void deleteTransactionByNameAndValueAndMonth(String name, float value, Month month);
}
