package com.calculator.services.receivers.transaction;

public interface TransactionAddable {
    public void addTransaction(String name, float amount, String month, Integer mccCode);
}
