package com.calculator.services.receivers.category;

import com.calculator.services.exceptions.CommandExecutionException;

import java.util.List;

public interface CategoryUpdatable {
    public void updateCategory(String name, List<Integer> mccCodes) throws CommandExecutionException;
}
