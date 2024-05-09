package com.calculator.services.receivers.transaction;

import java.time.Month;

public interface TransactionAddable {
    public void addTransaction(String name, float amount, Month month, Integer mccCode);
}
