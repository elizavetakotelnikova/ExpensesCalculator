package com.calculator.services.receivers.transaction;

import com.calculator.services.exceptions.CommandExecutionException;

import java.time.Month;

public interface TransactionDeletable {
    void deleteTransaction(String name, float amount, Month month) throws CommandExecutionException;
}
