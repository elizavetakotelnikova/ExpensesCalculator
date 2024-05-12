package com.calculator.services.commandReceivers.category;

import com.calculator.persistance.category.Category;
import com.calculator.persistance.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SubcategoriesManagerImpl implements SubcategoriesManager {
    private final CategoryRepository categoryRepository;
    public void findAllSubcategories(Category category, List<Long> usedIds, List<Category> subcategories) {
        usedIds.add(category.getId());
        for (var eachCategory : category.getSubcategories()) {
            if (!usedIds.contains(eachCategory.getId())) {
                subcategories.add(eachCategory);
                findAllSubcategories(eachCategory, usedIds, subcategories);
            }
        }
    }

    public void findAllParentCategories(Category category, List<Long> usedIds, List<Category> categories) {
        usedIds.add(category.getId());
        for (var eachCategory : categoryRepository.subcategories(category)) {
            if (!usedIds.contains(eachCategory.getId())) {
                categories.add(eachCategory);
                findAllParentCategories(eachCategory, new ArrayList<>(), categories);
            }
        }
    }
}
