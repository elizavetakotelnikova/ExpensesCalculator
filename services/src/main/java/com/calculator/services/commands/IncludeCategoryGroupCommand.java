package com.calculator.services.commands;

import com.calculator.services.exceptions.CommandExecutionException;
import com.calculator.services.exceptions.IncorrectArgumentsException;
import com.calculator.services.services.CategoryService;
import com.calculator.services.services.Validator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@AllArgsConstructor
public class IncludeCategoryGroupCommand implements Command {
    private String parentCategoryName;
    private List<String> subcategories;
    @Autowired
    private final CategoryService categoryService;
    @Autowired
    private final Validator validator;
    @Override
    public void execute() throws CommandExecutionException {
        categoryService.includeCategory(parentCategoryName, subcategories);
    }

    @Override
    public boolean validateArguments(List<String> arguments) throws IncorrectArgumentsException {
        if (arguments.size() < 2) throw new IncorrectArgumentsException("Not enough arguments");
        if (validator.validateCategoryName(arguments.getFirst())) throw new IncorrectArgumentsException("Invalid category name");
        //var validSubcategoriesNames = new ArrayList<>();
        for (int i = 1; i < arguments.size(); i++) {
            if (validator.validateCategoryName(arguments.get(i))) throw new IncorrectArgumentsException("Invalid subcategory name");
        }
        return true;
    }
}
