package com.calculator.services.services;

import com.calculator.persistance.category.CategoryRepository;
import com.calculator.persistance.transaction.Transaction;
import com.calculator.persistance.transaction.TransactionRepository;
import com.calculator.services.receivers.transaction.TransactionAddable;
import com.calculator.services.receivers.transaction.TransactionDeletable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Locale;

@Service
@AllArgsConstructor
public class TransactionService implements TransactionAddable, TransactionDeletable {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final Displayable display;

    @Override
    public void addTransaction(String name, float amount, String month, Integer mccCode) {
        Month givenMonth = Month.valueOf(month.toUpperCase());
        transactionRepository.save(new Transaction(name, amount, givenMonth, mccCode));
        var foundCategory = categoryRepository.findCategoryByMccCodes(new ArrayList<>(mccCode));
        var categoriesNames = new ArrayList<>();
        categoriesNames.add(foundCategory.getName());
        for (var each : foundCategory.getSubcategories()) {
            categoriesNames.add(each.getName());
        }
        display.displayMessage("Transaction added into category with names " + categoriesNames.stream().map(x -> x + " ").toString());
    }

    @Override
    public void deleteTransaction(String name, float amount, String month) {
        Month givenMonth = Month.valueOf(month.toUpperCase());
        if (transactionRepository.findTransactionByNameAndValueAndMonth(name, amount, givenMonth) == null) {
            display.displayMessage("No such transaction");
            return;
        }
        transactionRepository.deleteTransactionByNameAndValueAndMonth(name, amount, givenMonth);
        display.displayMessage("Transaction deleted");
    }
}
