package com.calculator.services.services;

import com.calculator.persistance.category.CategoryRepository;
import com.calculator.persistance.transaction.Transaction;
import com.calculator.persistance.transaction.TransactionRepository;
import com.calculator.services.receivers.transaction.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class TransactionManagerImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final Displayable display;

    @Override
    public void addTransaction(String name, float amount, Month month, Integer mccCode) {
        transactionRepository.save(new Transaction(name, amount, month, mccCode));
        var foundCategory = categoryRepository.findCategoryByMccCodeIn(new ArrayList<>(mccCode));
        var categoriesNames = new ArrayList<>();
        categoriesNames.add(foundCategory.getName());
        for (var each : foundCategory.getSubcategories()) {
            categoriesNames.add(each.getName());
        }
        display.displayMessage("Transaction added into category with names " + categoriesNames.stream().map(x -> x + " ").toString());
    }

    @Override
    public void deleteTransaction(String name, float amount, Month month) {
        if (transactionRepository.findTransactionByNameAndValueAndMonth(name, amount, month) == null) {
            display.displayMessage("No such transaction");
            return;
        }
        transactionRepository.deleteTransactionByNameAndValueAndMonth(name, amount, month);
        display.displayMessage("Transaction deleted");
    }
}
