package com.calculator.services.commandReceivers.transaction;

import com.calculator.services.exceptions.CommandExecutionException;

import java.time.Month;

public interface TransactionDeletable {
    void deleteTransaction(String name, float amount, Month month) throws CommandExecutionException;
}
