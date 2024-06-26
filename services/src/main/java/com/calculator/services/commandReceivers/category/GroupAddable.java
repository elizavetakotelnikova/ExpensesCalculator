package com.calculator.services.commandReceivers.category;

import com.calculator.services.exceptions.CommandExecutionException;

import java.util.List;

public interface GroupAddable {
    void addSubcategories(String parentCategory, List<String> categoriesToAdd) throws CommandExecutionException;
}
