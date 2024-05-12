package com.calculator.services.commands;
import com.calculator.services.exceptions.CommandExecutionException;
import com.calculator.services.exceptions.IncorrectArgumentsException;
import com.calculator.services.commandReceivers.category.CategoryCreatable;
import com.calculator.services.services.Validator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
public class AddCategoryCommand implements Command {
    private final CategoryCreatable categoryService;
    private final Validator validator;
    private String categoryName;
    private List<Integer> mccCodes;
    @Override
    public void execute() throws CommandExecutionException {
        categoryService.createCategory(categoryName, mccCodes);
    }

    @Override
    public boolean validateArguments(List<String> arguments) throws IncorrectArgumentsException {
        if (arguments.size() < 2) throw new IncorrectArgumentsException("Not enough arguments");
        if (!validator.validateCategoryName(arguments.getFirst())) throw new IncorrectArgumentsException("Invalid category name");
        for (int i = 1; i < arguments.size(); i++) {
            if (!validator.validateMccCode(arguments.get(i))) throw new IncorrectArgumentsException("Invalid mcc codes");
        }
        this.categoryName = arguments.getFirst();
        this.mccCodes = arguments.stream().skip(1).map(Integer::parseInt).toList();
        return true;
    }
}
