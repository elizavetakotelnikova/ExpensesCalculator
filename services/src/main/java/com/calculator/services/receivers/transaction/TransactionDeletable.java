package com.calculator.services.receivers.transaction;

public interface TransactionDeletable {
    void deleteTransaction(String name, float amount, String month);
}
