package com.calculator.persistance.category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findCategoriesByName(String name);
    //method to find Category, containing given subcategory in its list
    List<Category> subcategories(Category category);
    //method to find Category, containing given mccCode in its list
    Category mccCode(Integer mccCode);
    void deleteCategoryByName(String name);
}
