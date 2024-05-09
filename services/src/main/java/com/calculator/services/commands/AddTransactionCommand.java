package com.calculator.services.commands;

import com.calculator.services.exceptions.CommandExecutionException;
import com.calculator.services.exceptions.IncorrectArgumentsException;
import com.calculator.services.services.TransactionService;
import com.calculator.services.services.Validator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Month;
import java.util.List;

@AllArgsConstructor
public class AddTransactionCommand implements Command {
    private String name;
    private Float value;
    private String month; //может надо поменять на Month month
    private Integer mccCode;
    @Autowired
    private final TransactionService transactionService;
    @Autowired
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
        }
        return true;
    }
}
