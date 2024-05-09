package com.calculator.persistance.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    //Category save(Category category);
    List<Category> findCategoriesByName(String name);
    Category findCategoryById(Long id);
    Category findCategoryByMccCodes(List<Integer> mccCodes);
    Category findCategoryBySubcategories(List<Category> subcategories);
    void deleteCategoryById(Long id);
    void deleteCategoryByName(String name);
}
