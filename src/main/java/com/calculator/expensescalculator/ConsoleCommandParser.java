package com.calculator.expensescalculator;
import com.calculator.expensescalculator.exceptions.ParsingException;
import com.calculator.services.ChainLink;
import com.calculator.services.Request;
import com.calculator.services.parsingHandlers.ArgumentsHandler;
import com.calculator.services.parsingHandlers.CommandHandler;
import com.calculator.services.commands.Command;
import com.calculator.services.commands.CommandsDictionary;
import com.calculator.services.exceptions.IncorrectArgumentsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
@Component
@RequiredArgsConstructor
public class ConsoleCommandParser implements Parser {
    private final CommandsDictionary commandsDictionary;
    public Command parseCommand() throws ParsingException {
        List<String> arguments = getLine();
        Command parsedCommand;
        try {
            parsedCommand = defineCommand(arguments);
        }
        catch (IncorrectArgumentsException exception) {
            throw new ParsingException(exception.getMessage());
        }
        if (parsedCommand == null) throw new ParsingException("Command is not parsed");
        return parsedCommand;
    }

    public List<String> getLine() {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        if (line == null) return new ArrayList<>();
        String[] arguments = line.split(" ");
        return Arrays.stream(arguments).toList();
    }

    private Command defineCommand(List<String> arguments) throws IncorrectArgumentsException {
        if (arguments.isEmpty()) return null;
        ChainLink commandHandler = new CommandHandler(commandsDictionary).addNext(new ArgumentsHandler());
        var listArguments = new ArrayList<>(arguments);
        var parsingRequest = new Request(null, listArguments);
        commandHandler.handle(parsingRequest);
        return parsingRequest.getCommand();
    }
}