package com.calculator.persistance.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTransactionsByNameAndValueAndMonth(String name, float value, Month month);
    Transaction findTopTransactionsByNameAndValueAndMonth(String name, float value, Month month);
    List<Transaction> findTransactionsByMonth(Month month);
    void deleteTransactionByNameAndValueAndMonth(String name, float value, Month month);
}
