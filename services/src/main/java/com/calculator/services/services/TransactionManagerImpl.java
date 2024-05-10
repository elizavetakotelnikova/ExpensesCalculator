package com.calculator.services.services;

import com.calculator.persistance.category.Category;
import com.calculator.persistance.category.CategoryRepository;
import com.calculator.persistance.transaction.Transaction;
import com.calculator.persistance.transaction.TransactionRepository;
import com.calculator.services.receivers.transaction.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;

@Service
@AllArgsConstructor
@Transactional
public class TransactionManagerImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final Displayable display;

    @Override
    public void addTransaction(String name, float amount, Month month, Integer mccCode) {
        transactionRepository.save(new Transaction(name, amount, month, mccCode));
        var foundCategory = categoryRepository.mccCode(mccCode);
        if (foundCategory == null) {
            display.displayMessage("Transaction added into category with name Without category");
            return;
        }
        var foundParentCategories = categoryRepository.subcategories(foundCategory);
        var listOfNames = new ArrayList<>();
        listOfNames.add(foundCategory.getName());
        listOfNames.addAll(foundParentCategories.stream().map(Category::getName).toList());
        display.displayMessage("Transaction added into category with names " + Arrays.toString(listOfNames.toArray()));
    }

    @Override
    public void deleteTransaction(String name, float amount, Month month) {
        if (transactionRepository.findTransactionsByNameAndValueAndMonth(name, amount, month).isEmpty()) {
            display.displayMessage("No such transaction");
            return;
        }
        var transaction = transactionRepository.findTopTransactionsByNameAndValueAndMonth(name, amount, month);
        transactionRepository.deleteById(transaction.getId());
        //transactionRepository.deleteTransactionByNameAndValueAndMonth(name, amount, month);
        display.displayMessage("Transaction deleted");
    }
}
