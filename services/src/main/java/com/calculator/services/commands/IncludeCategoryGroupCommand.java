package com.calculator.services.commands;

import com.calculator.services.exceptions.CommandExecutionException;
import com.calculator.services.exceptions.IncorrectArgumentsException;
import com.calculator.services.commandReceivers.category.GroupAddable;
import com.calculator.services.services.Validator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
public class IncludeCategoryGroupCommand implements Command {
    private final GroupAddable categoryService;
    private final Validator validator;
    private String parentCategoryName;
    private List<String> subcategories;
    @Override
    public void execute() throws CommandExecutionException {
        categoryService.addSubcategories(parentCategoryName, subcategories);
    }

    @Override
    public boolean validateArguments(List<String> arguments) throws IncorrectArgumentsException {
        if (arguments.size() < 2) throw new IncorrectArgumentsException("Not enough arguments");
        if (!validator.validateCategoryName(arguments.getFirst())) throw new IncorrectArgumentsException("Invalid category name");
        for (int i = 1; i < arguments.size(); i++) {
            if (!validator.validateCategoryName(arguments.get(i))) throw new IncorrectArgumentsException("Invalid subcategory name");
        }
        this.parentCategoryName = arguments.getFirst();
        this.subcategories = arguments.stream().skip(1).toList();
        return true;
    }
}
