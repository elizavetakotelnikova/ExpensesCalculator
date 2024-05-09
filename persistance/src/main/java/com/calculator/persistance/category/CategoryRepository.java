package com.calculator.persistance.category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findCategoriesByName(String name);
    Category findCategoryById(Long id);
    Category findCategoryByMccCodeIn(List<Integer> mccCodes);

    //method to find Category, containing given mccCode in its list
    Category mccCode(Integer mccCode);
    Category findCategoryBySubcategories(List<Category> subcategories);
    void deleteCategoryById(Long id);
    void deleteCategoryByName(String name);
}
