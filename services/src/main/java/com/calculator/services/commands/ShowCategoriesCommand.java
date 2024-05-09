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
public class ShowCategoriesCommand implements Command {
    private final CategoryService categoryService;
    @Override
    public void execute() throws CommandExecutionException {
        categoryService.showCategories();
    }

    @Override
    public boolean validateArguments(List<String> arguments) throws IncorrectArgumentsException {
        return true;
    }
}
