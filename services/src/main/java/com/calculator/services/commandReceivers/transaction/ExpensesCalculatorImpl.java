package com.calculator.services.commandReceivers.transaction;
import com.calculator.persistance.category.Category;
import com.calculator.persistance.category.CategoryRepository;
import com.calculator.persistance.transaction.Transaction;
import com.calculator.persistance.transaction.TransactionRepository;
import com.calculator.services.exceptions.CommandExecutionException;
import com.calculator.services.services.Displayable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExpensesCalculatorImpl implements ExpensesCalculator {
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final Displayable display;
    private Map<String, Float> calculateCategoriesExpensesByTransactions(List<Transaction> transactionList) {
        Map<String, Float> sumOfExpenses = new HashMap<>();
        for (var transaction : transactionList) {
            var foundCategory = categoryRepository.mccCode(transaction.getMccCode());
            var categoryName = "Без категории";
            if (foundCategory != null) categoryName = foundCategory.getName();
            if (!sumOfExpenses.containsKey(categoryName)) {
                sumOfExpenses.put(categoryName, transaction.getValue());
            } else {
                sumOfExpenses.put(categoryName, sumOfExpenses.get(categoryName) + transaction.getValue());
            }
        }
        return sumOfExpenses;
    }
    private void findAllSubcategories(Category category, List<Long> usedIds, List<Category> subcategories) {
        usedIds.add(category.getId());
        for (var eachCategory : category.getSubcategories()) {
            if (!usedIds.contains(eachCategory.getId())) {
                subcategories.add(eachCategory);
                findAllSubcategories(eachCategory, usedIds, subcategories);
            }
        }
    }

    @Override
    public void showExpensesByMonth(Month month) {
        // finding all transaction in a given month
        var transactionsByMonth = transactionRepository.findTransactionsByMonth(month);
        Map<String, Float> sumOfExpenses = calculateCategoriesExpensesByTransactions(transactionsByMonth);
        var totalSum = sumOfExpenses.values().stream().mapToDouble(x -> x).sum();
        // calculating sum of each category's expenses in a given month
        for (var category : categoryRepository.findAll()) {
            var subcategories = new ArrayList<Category>();
            findAllSubcategories(category, new ArrayList<>(), subcategories);
            var sum = subcategories.stream().mapToDouble(x -> sumOfExpenses.getOrDefault(x.getName(), 0.0F)).sum() +
                    sumOfExpenses.getOrDefault(category.getName(), 0.0F);
            display.displayMessage(category.getName() + " " + sum + " " + "рублей" + " " + Math.round(sum/totalSum * 100 * 100)/100.00 + "%");
        }
        // default category
        var sum = sumOfExpenses.getOrDefault("Без категории", 0.0F);
        display.displayMessage("Без категории" + " " + sum + " " + "рублей" + " " + Math.round(sum/totalSum * 100 * 100)/100.00 + "%");
    }

    @Override
    public void showMonthlyExpenses(String category) throws CommandExecutionException {
        var foundCategory = categoryRepository.findCategoryByName(category);
        if (foundCategory == null) throw new CommandExecutionException("No such category");
        var subcategories = new ArrayList<Category>();
        findAllSubcategories(foundCategory, new ArrayList<>(), subcategories);
        var mccCodes = new ArrayList<>();
        subcategories.forEach(x-> mccCodes.addAll(x.getMccCode()));
        mccCodes.addAll(foundCategory.getMccCode());
        for (var each : Month.values()) {
            var transactionsByMonth = transactionRepository.findTransactionsByMonth(each);
            var sum = transactionsByMonth.stream().filter(x -> mccCodes.contains(x.getMccCode())).
                    mapToDouble(Transaction::getValue).sum();
            display.displayMessage(each.getDisplayName(TextStyle.FULL_STANDALONE , new Locale("ru")) + " " + sum + " " + "рублей");
        }
    }
}
