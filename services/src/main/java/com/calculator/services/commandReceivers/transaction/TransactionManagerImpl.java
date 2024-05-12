package com.calculator.services.commandReceivers.transaction;

import com.calculator.persistance.category.Category;
import com.calculator.persistance.category.CategoryRepository;
import com.calculator.persistance.transaction.Transaction;
import com.calculator.persistance.transaction.TransactionRepository;
import com.calculator.services.commandReceivers.category.SubcategoriesManager;
import com.calculator.services.exceptions.CommandExecutionException;
import com.calculator.services.commandReceivers.transaction.TransactionAddable;
import com.calculator.services.commandReceivers.transaction.TransactionDeletable;
import com.calculator.services.services.Displayable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class TransactionManagerImpl implements TransactionAddable, TransactionDeletable {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoriesManager subcategoriesManager;
    private final Displayable display;

    @Override
    public void addTransaction(String name, float amount, Month month, Integer mccCode) {
        transactionRepository.save(new Transaction(name, amount, month, mccCode));
        var foundCategory = categoryRepository.mccCode(mccCode);
        if (foundCategory == null) {
            display.displayMessage("Transaction added into category with name Без категории");
            return;
        }
        var parentCategories = new ArrayList<Category>();
        parentCategories.add(foundCategory);
        subcategoriesManager.findAllParentCategories(foundCategory, new ArrayList<>(), parentCategories);
        var listOfNames = parentCategories.stream().map(Category::getName).toList();
        display.displayMessage("Transaction added into category with names " + Arrays.toString(listOfNames.toArray()));
    }

    @Override
    public void deleteTransaction(String name, float amount, Month month) throws CommandExecutionException {
        var foundTransactions =  transactionRepository.findTransactionsByNameAndValueAndMonth(name, amount, month);
        if (foundTransactions.isEmpty()) {
            //display.displayMessage("No such transaction");
            throw new CommandExecutionException("No such transaction");
        }
        var transaction = transactionRepository.findTopTransactionsByNameAndValueAndMonth(name, amount, month);
        transactionRepository.deleteById(transaction.getId());
        display.displayMessage("Transaction deleted");
    }
}
