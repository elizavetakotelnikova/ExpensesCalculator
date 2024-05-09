package com.calculator.services.commands;
import com.calculator.services.exceptions.CommandExecutionException;
import com.calculator.services.exceptions.IncorrectArgumentsException;
import com.calculator.services.receivers.category.CategoryShowable;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ShowCategoriesCommand implements Command {
    private final CategoryShowable categoryService;
    @Override
    public void execute() throws CommandExecutionException {
        categoryService.showCategories();
    }

    @Override
    public boolean validateArguments(List<String> arguments) throws IncorrectArgumentsException {
        return true;
    }
}
