package com.calculator.services.commands;

import com.calculator.services.exceptions.CommandExecutionException;
import com.calculator.services.exceptions.IncorrectArgumentsException;
import com.calculator.services.commandReceivers.transaction.TransactionDeletable;
import com.calculator.services.services.Validator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.Month;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
public class DeleteTransactionCommand implements Command {
    private final TransactionDeletable transactionService;
    private final Validator validator;
    private String name;
    private Float value;
    private Month month;
    @Override
    public void execute() throws CommandExecutionException {
        transactionService.deleteTransaction(name, value, month);
    }

    @Override
    public boolean validateArguments(List<String> arguments) throws IncorrectArgumentsException {
        if (arguments.size() < 3) throw new IncorrectArgumentsException("Not enough arguments");
        if (!validator.validateTransactionName(arguments.getFirst())) throw new IncorrectArgumentsException("Invalid transaction name");
        if (!validator.validateAmount(arguments.get(1))) throw new IncorrectArgumentsException("Invalid amount");
        if (!validator.validateMonth(arguments.get(2))) throw new IncorrectArgumentsException("Invalid month name");
        this.name = arguments.getFirst();
        this.value = Float.parseFloat(arguments.get(1));
        this.month = Month.valueOf(arguments.get(2).toUpperCase());
        return true;
    }
}
