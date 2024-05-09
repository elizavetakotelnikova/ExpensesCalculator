package com.calculator.services.exceptions;

public class CommandExecutionException extends Exception
{
    public CommandExecutionException() {}

    public CommandExecutionException(String message)
    {
        super(message);
    }
}
