package com.calculator.services.services;

import com.calculator.persistance.category.Category;
import com.calculator.persistance.category.CategoryRepository;
import com.calculator.services.exceptions.CommandExecutionException;
import com.calculator.services.receivers.category.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@Service
public class CategoryService implements CategoryCreatable, CategoryUpdatable, GroupAddable,
        CategoryDeletable, CategoryShowable {
    private final CategoryRepository categoryRepository;
    private final Displayable display;
    @Override
    public void createCategory(String name, List<Integer> mccCodes) throws CommandExecutionException {
        for (Integer code : mccCodes) {
            var foundCategory = categoryRepository.findCategoryByMccCodes(new ArrayList<>(code));
            if (foundCategory != null) throw new CommandExecutionException("Mcc already reserved for category " + foundCategory.getName());
            categoryRepository.save(new Category(name, mccCodes));
            display.displayMessage("Created new category " + foundCategory.getName() + "with codes " + mccCodes.stream().map(x -> x.toString() + " ").toString());
        }
    }

    @Override
    public void updateCategory(String name, List<Integer> mccCodes) throws CommandExecutionException {
        Category foundCategoryByName = categoryRepository.findCategoriesByName(name).getFirst();
        if (foundCategoryByName == null) throw new CommandExecutionException("Such category doesn't exist yet");
        var notAddedCodes = new ArrayList<>();
        for (var code : mccCodes) {
            var foundCategory = categoryRepository.findCategoryByMccCodes(new ArrayList<>(code));
            if (foundCategory != null) notAddedCodes.add("code " + code.toString() + " already reserved for another category " + foundCategory.getName() + " ");
            else {
                foundCategoryByName.getMccCodes().add(code);
            }
        }
        if (!notAddedCodes.isEmpty()) throw new CommandExecutionException(notAddedCodes.toString());
        categoryRepository.save(foundCategoryByName);
        display.displayMessage("Added new mcc to category " + foundCategoryByName.getName() + "with codes " + mccCodes.stream().map(x -> x.toString() + " ").toString());
    }

    @Override
    public void includeCategory(String parentCategory, List<String> categoriesToAdd) throws CommandExecutionException {
        var foundParentCategory = categoryRepository.findCategoriesByName(parentCategory).getFirst();
        if (foundParentCategory == null) throw new CommandExecutionException("No such parent category");
        var notAddedCategories = new ArrayList<>();
        var allAddedCodes = new ArrayList<>();
        for (var subcategory : categoriesToAdd) {
            var foundCategory = categoryRepository.findCategoriesByName(subcategory).getFirst();
            if (foundCategory == null) notAddedCategories.add("Category " + subcategory + " doesn't exist ");
            else {
                foundParentCategory.getSubcategories().add(foundCategory);
                allAddedCodes.add(foundCategory.getMccCodes());
            }
        }
        if (!notAddedCategories.isEmpty()) throw new CommandExecutionException(notAddedCategories.toString());
        categoryRepository.save(foundParentCategory);
        display.displayMessage("added new group to " + foundParentCategory.getName() + "with codes " + allAddedCodes.stream().map(x -> x.toString() + " ").toString() + categoriesToAdd.stream().map(x -> x + " ").toString());
    }

    @Override
    public void deleteCategory(String name) {
        categoryRepository.deleteCategoryByName(name);
    }

    @Override
    public void showCategories() {
        var categories = categoryRepository.findAll();
        categories.forEach(x -> display.displayMessage(x.getName()));
    }
}
