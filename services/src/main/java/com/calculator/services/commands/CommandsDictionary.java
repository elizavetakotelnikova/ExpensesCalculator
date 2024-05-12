package com.calculator.services.commands;

import com.calculator.services.commandReceivers.category.*;
import com.calculator.services.commandReceivers.transaction.ExpensesCalculator;
import com.calculator.services.commandReceivers.transaction.TransactionAddable;
import com.calculator.services.commandReceivers.transaction.TransactionDeletable;
import com.calculator.services.services.Validator;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class CommandsDictionary {
    private final ApplicationContext applicationContext;
    @Getter
    private Map<String, Command> commandMap = new HashMap<>();
    @Autowired
    public CommandsDictionary(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        initializeMap();
    }
    private void initializeMap() {
        commandMap.put("add category", new AddCategoryCommand(applicationContext.getBean(CategoryCreatable.class),
                applicationContext.getBean(Validator.class)));
        commandMap.put("add mcc to category", new UpdateCategoryCommand(applicationContext.getBean(CategoryUpdatable.class),
                applicationContext.getBean(Validator.class)));
        commandMap.put("add group to category", new IncludeCategoryGroupCommand(applicationContext.getBean(GroupAddable.class),
                applicationContext.getBean(Validator.class)));
        commandMap.put("remove category", new DeleteCategoryCommand(applicationContext.getBean(CategoryDeletable.class),
                applicationContext.getBean(Validator.class)));
        commandMap.put("add transaction", new AddTransactionCommand(applicationContext.getBean(TransactionAddable.class),
                applicationContext.getBean(Validator.class)));
        commandMap.put("remove transaction", new DeleteTransactionCommand(applicationContext.getBean(TransactionDeletable.class),
                applicationContext.getBean(Validator.class)));
        commandMap.put("show categories", new ShowCategoriesCommand(applicationContext.getBean(CategoryShowable.class)));
        commandMap.put("show expenses in", new ShowAllExpensesByMonth(applicationContext.getBean(ExpensesCalculator.class),
                applicationContext.getBean(Validator.class)));
        commandMap.put("show monthly expenses in", new ShowMonthlyCategoryExpenses(applicationContext.getBean(ExpensesCalculator.class),
                applicationContext.getBean(Validator.class)));
    }
}
