package com.calculator.services;

import com.calculator.services.commands.Command;
import com.calculator.services.exceptions.CommandExecutionException;

public interface CommandInvoker {
    void consume(Command command) throws CommandExecutionException;
}
