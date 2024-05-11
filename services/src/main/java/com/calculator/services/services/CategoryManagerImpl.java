package com.calculator.services.services;

import com.calculator.persistance.category.Category;
import com.calculator.persistance.category.CategoryRepository;
import com.calculator.services.exceptions.CommandExecutionException;
import com.calculator.services.receivers.category.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CategoryManagerImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final Displayable display;
    @Override
    public void createCategory(String name, List<Integer> mccCodes) throws CommandExecutionException {
        for (Integer code : mccCodes) {
            var foundCategory = categoryRepository.mccCode(code);
            if (foundCategory != null) throw new CommandExecutionException("Mcc already reserved for category " + foundCategory.getName());
        }
        categoryRepository.save(new Category(name, mccCodes));
        display.displayMessage("Created new category " + name + " with codes " + Arrays.toString(mccCodes.toArray()));
    }

    @Override
    public void updateCategory(String name, List<Integer> mccCodes) throws CommandExecutionException {
        Category foundCategoryByName = categoryRepository.findCategoriesByName(name).getFirst();
        if (foundCategoryByName == null) throw new CommandExecutionException("Such category doesn't exist yet");
        var notAddedCodes = new ArrayList<>();
        for (var code : mccCodes) {
            var foundCategory = categoryRepository.mccCode(code);
            if (foundCategory != null) notAddedCodes.add("Mcc " + code + " already reserved for another category " + foundCategory.getName() + " ");
            else {
                foundCategoryByName.getMccCode().add(code);
            }
        }
        if (!notAddedCodes.isEmpty()) throw new CommandExecutionException(notAddedCodes.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", ")));
        categoryRepository.save(foundCategoryByName);
        display.displayMessage("Added new mcc to category " + foundCategoryByName.getName() + "with codes " + Arrays.toString(mccCodes.toArray()));
    }

    @Override
    public void addCategoriesGroup(String parentCategory, List<String> categoriesToAdd) throws CommandExecutionException {
        var foundParentCategory = categoryRepository.findCategoriesByName(parentCategory).getFirst();
        if (foundParentCategory == null) throw new CommandExecutionException("No such parent category");
        var notAddedCategories = new ArrayList<>();
        var allAddedCodes = new ArrayList<>();
        for (var subcategory : categoriesToAdd) {
            var foundCategory = categoryRepository.findCategoriesByName(subcategory).getFirst();
            if (foundCategory == null) notAddedCategories.add("Category " + subcategory + " doesn't exist ");
            else {
                foundParentCategory.getSubcategories().add(foundCategory);
                allAddedCodes.add(foundCategory.getMccCode());
            }
        }
        if (!notAddedCategories.isEmpty()) throw new CommandExecutionException(notAddedCategories.toString());
        categoryRepository.save(foundParentCategory);
        display.displayMessage("Added new group to " + foundParentCategory.getName() + " with codes " + Arrays.toString(allAddedCodes.toArray()) + " " + Arrays.toString(categoriesToAdd.toArray()));
    }

    @Override
    @Transactional
    public void deleteCategory(String name) throws CommandExecutionException {
        var foundCategory = categoryRepository.findCategoriesByName(name);
        if (foundCategory.isEmpty()) throw new CommandExecutionException("No such category");
        /*var parentCategories = categoryRepository.subcategories(foundCategory.getFirst());
        if (!parentCategories.isEmpty()) parentCategories.forEach(x -> x.getSubcategories().remove(foundCategory));*/
        categoryRepository.deleteCategoryByName(name);
    }

    @Override
    public void showCategories() {
        var categories = categoryRepository.findAll();
        categories.forEach(x -> display.displayMessage(x.getName()));
    }
}
