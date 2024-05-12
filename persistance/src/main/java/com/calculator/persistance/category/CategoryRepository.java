package com.calculator.persistance.category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findCategoriesByName(String name);

    /**
     * method to find all categories which include given category
     * @param category subcategory to find in lists of  subcategories
     * @return list of categories
     */
    List<Category> subcategories(Category category);

    /**
     * method to find category which given mccCode in its mccCodes list
     * @param mccCode mccCode to find
     * @return category with given mccCode
     */
    Category mccCode(Integer mccCode);
    void deleteCategoryByName(String name);
}
