package com.calculator.services.commandReceivers.category;

import com.calculator.persistance.category.Category;

import java.util.List;

public interface SubcategoriesManager {
    void findAllSubcategories(Category category, List<Long> usedIds, List<Category> subcategories);
    void findAllParentCategories(Category category, List<Long> usedIds, List<Category> categories);
}
