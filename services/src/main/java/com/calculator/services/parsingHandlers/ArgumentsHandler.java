package com.calculator.services.parsingHandlers;
import com.calculator.services.Request;
import com.calculator.services.ResponsibilityChainBase;
import com.calculator.services.exceptions.IncorrectArgumentsException;
import java.util.ArrayList;

public class ArgumentsHandler extends ResponsibilityChainBase {
    @Override
    public  void handle(Request request) throws IncorrectArgumentsException {
        if (request == null || request.getCommand() == null) throw new IncorrectArgumentsException("Invalid request");
        var arguments = new ArrayList<>(request.getTokenizedLine());
        ///?
        arguments.forEach(x -> request.getTokenizedLine().remove(x));
        if (request.getCommand().validateArguments(arguments)) {
            if (next != null) next.handle(request);
            return;
        }
        throw new IncorrectArgumentsException("Invalid command");
    }
}
