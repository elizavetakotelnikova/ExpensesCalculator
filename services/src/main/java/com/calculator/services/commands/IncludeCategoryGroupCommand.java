package com.calculator.services.commands;

import com.calculator.services.exceptions.CommandExecutionException;
import com.calculator.services.exceptions.IncorrectArgumentsException;
import com.calculator.services.receivers.category.GroupAddable;
import com.calculator.services.services.Validator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
public class IncludeCategoryGroupCommand implements Command {
    private String parentCategoryName;
    private List<String> subcategories;
    private final GroupAddable categoryService;
    private final Validator validator;
    @Override
    public void execute() throws CommandExecutionException {
        categoryService.addCategoriesGroup(parentCategoryName, subcategories);
    }

    @Override
    public boolean validateArguments(List<String> arguments) throws IncorrectArgumentsException {
        if (arguments.size() < 2) throw new IncorrectArgumentsException("Not enough arguments");
        if (validator.validateCategoryName(arguments.getFirst())) throw new IncorrectArgumentsException("Invalid category name");
        //var validSubcategoriesNames = new ArrayList<>();
        for (int i = 1; i < arguments.size(); i++) {
            if (validator.validateCategoryName(arguments.get(i))) throw new IncorrectArgumentsException("Invalid subcategory name");
        }
        this.parentCategoryName = arguments.getFirst();
        arguments.removeFirst();
        this.subcategories = arguments;
        return true;
    }
}
