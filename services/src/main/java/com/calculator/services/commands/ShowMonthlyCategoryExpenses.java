package com.calculator.services.commands;
import com.calculator.services.exceptions.CommandExecutionException;
import com.calculator.services.exceptions.IncorrectArgumentsException;
import com.calculator.services.commandReceivers.transaction.ExpensesCalculator;
import com.calculator.services.services.Validator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
public class ShowMonthlyCategoryExpenses implements Command {
    private final ExpensesCalculator expensesCalculator;
    private final Validator validator;
    private String category;
    @Override
    public void execute() throws CommandExecutionException {
        expensesCalculator.showMonthlyExpenses(category);
    }

    @Override
    public boolean validateArguments(List<String> arguments) throws IncorrectArgumentsException {
        if (arguments.isEmpty()) throw new IncorrectArgumentsException("Not enough arguments");
        if (!validator.validateCategoryName(arguments.getFirst())) throw new IncorrectArgumentsException("Invalid category name");
        this.category = arguments.getFirst();
        return true;
    }
}
