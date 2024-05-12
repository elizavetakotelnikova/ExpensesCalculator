package com.calculator.services.commandReceivers.category;

import com.calculator.persistance.category.Category;
import com.calculator.persistance.category.CategoryRepository;
import com.calculator.services.exceptions.CommandExecutionException;
import com.calculator.services.commandReceivers.category.*;
import com.calculator.services.services.Displayable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CategoryManagerImpl implements CategoryCreatable, CategoryDeletable,
        CategoryUpdatable, CategoryShowable, GroupAddable {
    private final CategoryRepository categoryRepository;
    private final SubcategoriesManager subcategoriesManager;
    private final Displayable display;
    @Override
    public void createCategory(String name, List<Integer> mccCodes) throws CommandExecutionException {
        for (Integer code : mccCodes) {
            var foundCategory = categoryRepository.mccCode(code);
            if (foundCategory != null) throw new CommandExecutionException("Mcc already reserved for category " + foundCategory.getName());
        }
        // in case we have unique categories name (в примечании docs об этом написала)
        if (categoryRepository.findCategoryByName(name) != null) throw new CommandExecutionException("Category with given name already exists");
        categoryRepository.save(new Category(name, mccCodes));
        display.displayMessage("Created new category " + name + " with codes " + Arrays.toString(mccCodes.toArray()));
    }

    @Override
    public void updateCategory(String name, List<Integer> mccCodes) throws CommandExecutionException {
        Category foundCategoryByName = categoryRepository.findCategoryByName(name);
        if (foundCategoryByName == null) throw new CommandExecutionException("Such category doesn't exist yet");
        var notAddedCodes = new ArrayList<>();
        for (var code : mccCodes) {
            var foundCategory = categoryRepository.mccCode(code);
            if (foundCategory != null) notAddedCodes.add("Mcc " + code + " already reserved for another category " + foundCategory.getName());
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
    public void addSubcategories(String parentCategory, List<String> categoriesToAdd) throws CommandExecutionException {
        var foundParentCategory = categoryRepository.findCategoryByName(parentCategory);
        if (foundParentCategory == null) throw new CommandExecutionException("No such parent category");
        var notAddedCategories = new ArrayList<>();
        var allAddedCodes = new ArrayList<>();
        for (var subcategory : categoriesToAdd) {
            var foundCategory = categoryRepository.findCategoryByName(subcategory);
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
        var foundCategory = categoryRepository.findCategoryByName(name);
        if (foundCategory == null) throw new CommandExecutionException("No such category");
        var listOfAllParentCategories = new ArrayList<Category>();
        subcategoriesManager.findAllParentCategories(foundCategory, new ArrayList<>(), listOfAllParentCategories);
        var parentCategories = categoryRepository.subcategories(foundCategory);
        if (!parentCategories.isEmpty()) {
            parentCategories.forEach(x -> x.getSubcategories().addAll(foundCategory.getSubcategories()));
            parentCategories.forEach(x -> x.getSubcategories().remove(foundCategory));
        }
        categoryRepository.deleteCategoryByName(name);
        display.displayMessage("Category removed from " +
                Arrays.toString(listOfAllParentCategories.stream().map(Category::getName).toArray()));
    }

    @Override
    public void showCategories() {
        var categories = categoryRepository.findAll();
        categories.forEach(x -> display.displayMessage(x.getName()));
    }
}
