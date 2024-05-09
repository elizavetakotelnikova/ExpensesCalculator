package com.calculator.services.commands;
import com.calculator.services.exceptions.CommandExecutionException;
import com.calculator.services.exceptions.IncorrectArgumentsException;
import com.calculator.services.services.CategoryService;
import com.calculator.services.services.Validator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
public class UpdateCategoryCommand implements Command {
    private String newCategoryName;
    private List<Integer> mccCodes;
    private final CategoryService categoryService;
    private final Validator validator;
    @Override
    public void execute() throws CommandExecutionException {
        categoryService.updateCategory(newCategoryName, mccCodes);
    }

    @Override
    public boolean validateArguments(List<String> arguments) throws IncorrectArgumentsException {
        if (arguments.size() != 1) throw new IncorrectArgumentsException("Not enough arguments");
        if (!validator.validateCategoryName(arguments.getFirst())) throw new IncorrectArgumentsException("Invalid category name");
        for (int i = 1; i < arguments.size(); i++) {
            if (!arguments.get(i).matches("[0-9]{4}")) throw new IncorrectArgumentsException("Invalid mcc codes");
        }
        return true;
    }
}
