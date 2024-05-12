package com.calculator.services.commands;
import com.calculator.services.exceptions.CommandExecutionException;
import com.calculator.services.exceptions.IncorrectArgumentsException;
import com.calculator.services.commandReceivers.transaction.ExpensesCalculator;
import com.calculator.services.services.Validator;
import lombok.RequiredArgsConstructor;

import java.time.Month;
import java.util.List;

@RequiredArgsConstructor
public class ShowAllExpensesByMonth implements Command {
    private final ExpensesCalculator expensesCalculator;
    private final Validator validator;
    private Month month;
    @Override
    public void execute() throws CommandExecutionException {
        expensesCalculator.showExpensesByMonth(month);
    }

    @Override
    public boolean validateArguments(List<String> arguments) throws IncorrectArgumentsException {
        if (arguments.isEmpty()) throw new IncorrectArgumentsException("Not enough arguments");
        if (!validator.validateMonth(arguments.getFirst())) throw new IncorrectArgumentsException("Invalid category name");
        this.month = Month.valueOf(arguments.getFirst().toUpperCase());
        return true;
    }
}
