package com.calculator.services.commands;

import com.calculator.services.exceptions.CommandExecutionException;
import com.calculator.services.exceptions.IncorrectArgumentsException;
import com.calculator.services.receivers.transaction.TransactionAddable;
import com.calculator.services.services.Validator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.Month;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
public class AddTransactionCommand implements Command {
    private String name;
    private Float value;
    private Month month;
    private Integer mccCode;
    private final TransactionAddable transactionService;
    private final Validator validator;
    @Override
    public void execute() throws CommandExecutionException {
        transactionService.addTransaction(name, value, month, mccCode);
    }

    @Override
    public boolean validateArguments(List<String> arguments) throws IncorrectArgumentsException {
        if (arguments.size() < 3) throw new IncorrectArgumentsException("Not enough arguments");
        if (!validator.validateTransactionName(arguments.getFirst())) throw new IncorrectArgumentsException("Invalid transaction name");
        if (!validator.validateAmount(arguments.get(1))) throw new IncorrectArgumentsException("Invalid amount");
        if (!validator.validateMonth(arguments.get(2))) throw new IncorrectArgumentsException("Invalid month name");
        if (arguments.size() == 4) {
            if (!validator.validateMccCode(arguments.get(3))) throw new IncorrectArgumentsException("Invalid mcc code");
            this.mccCode = Integer.parseInt(arguments.get(3));
        }
        this.name = arguments.getFirst();
        this.value = Float.parseFloat(arguments.get(1));
        this.month = Month.valueOf(arguments.get(2));
        return true;
    }
}
