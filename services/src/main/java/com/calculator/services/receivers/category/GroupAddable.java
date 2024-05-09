package com.calculator.services.receivers.category;

import com.calculator.services.exceptions.CommandExecutionException;

import java.util.List;

public interface GroupAddable {
    public void includeCategory(String parentCategory, List<String> categoriesToAdd) throws CommandExecutionException;
}
