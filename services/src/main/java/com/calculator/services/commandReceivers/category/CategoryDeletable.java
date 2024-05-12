package com.calculator.services.commandReceivers.category;

import com.calculator.services.exceptions.CommandExecutionException;

public interface CategoryDeletable {
    public void deleteCategory(String name) throws CommandExecutionException;
}
