package com.calculator.services;

import com.calculator.services.commands.Command;
import com.calculator.services.exceptions.CommandExecutionException;
import org.springframework.stereotype.Component;

@Component
public class CommandInvokerImpl implements CommandInvoker {
    public void consume(Command command) throws CommandExecutionException {
        if (command == null) throw new CommandExecutionException("No such command");
        command.execute();
    }
}
