package com.calculator.services.commandReceivers.transaction;

import com.calculator.services.exceptions.CommandExecutionException;

import java.time.Month;

public interface ExpensesCalculator {
    void showExpensesByMonth(Month month);
    void showMonthlyExpenses(String category) throws CommandExecutionException;
}
