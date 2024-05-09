package com.calculator.services.commands;

import com.calculator.services.exceptions.CommandExecutionException;
import com.calculator.services.exceptions.IncorrectArgumentsException;

import java.util.List;

public interface Command {
    void execute() throws CommandExecutionException;
    boolean validateArguments(List<String> arguments) throws IncorrectArgumentsException;
}
