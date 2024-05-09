package com.calculator.services.commands;

import com.calculator.persistance.category.CategoryRepository;
import com.calculator.services.exceptions.CommandExecutionException;
import com.calculator.services.exceptions.IncorrectArgumentsException;
import com.calculator.services.services.CategoryService;
import com.calculator.services.services.Validator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@AllArgsConstructor
public class DeleteCategoryCommand implements Command {
    private String category;
    @Autowired
    private final CategoryService categoryService;
    @Autowired
    private final Validator validator;
    @Override
    public void execute() throws CommandExecutionException {
        categoryService.deleteCategory(category);
    }

    @Override
    public boolean validateArguments(List<String> arguments) throws IncorrectArgumentsException {
        if (arguments.isEmpty()) throw new IncorrectArgumentsException("Not enough arguments");
        if (!validator.validateCategoryName(arguments.getFirst())) throw new IncorrectArgumentsException("Invalid category name");
        return true;
    }
}
