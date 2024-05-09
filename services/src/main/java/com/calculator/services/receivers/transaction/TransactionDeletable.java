package com.calculator.services.receivers.transaction;

import java.time.Month;

public interface TransactionDeletable {
    void deleteTransaction(String name, float amount, Month month);
}
