package com.calculator.expensescalculator;

import com.calculator.expensescalculator.exceptions.ParsingException;
import com.calculator.services.commands.Command;

import java.util.List;

public interface Parser
{
    public List<String> getLine();
    public Command parseCommand() throws ParsingException;
}
