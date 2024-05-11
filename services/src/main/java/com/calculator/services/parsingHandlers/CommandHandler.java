package com.calculator.services.parsingHandlers;

import com.calculator.services.Request;
import com.calculator.services.ResponsibilityChainBase;
import com.calculator.services.commands.CommandsDictionary;
import com.calculator.services.exceptions.IncorrectArgumentsException;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
public class CommandHandler extends ResponsibilityChainBase {
    private final CommandsDictionary commandDictionary;
    @Override
    public void handle(Request request) throws IncorrectArgumentsException {
        if (request == null) throw new IncorrectArgumentsException("Wrong request");
        if (request.getTokenizedLine().isEmpty()) throw new IncorrectArgumentsException("Command is not set");
        String commandPart = "";
        var listToRemove = new ArrayList<String>();
        for (var part : request.getTokenizedLine()) {
            commandPart += part;
            var valueFromMap = commandDictionary.getCommandMap().getOrDefault(commandPart, null);
            if (valueFromMap != null) {
                request.setCommand(valueFromMap);
                listToRemove.add(part);
                request.getTokenizedLine().removeAll(listToRemove);
                if (next != null) next.handle(request);
                return;
            }
            listToRemove.add(part);
            commandPart += " ";
        }
        throw new IncorrectArgumentsException("Invalid command");
    }
}
