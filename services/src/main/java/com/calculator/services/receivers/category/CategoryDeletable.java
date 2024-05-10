package com.calculator.services.receivers.category;

import com.calculator.persistance.category.Category;
import com.calculator.services.exceptions.CommandExecutionException;

public interface CategoryDeletable {
    public void deleteCategory(String name) throws CommandExecutionException;
}
