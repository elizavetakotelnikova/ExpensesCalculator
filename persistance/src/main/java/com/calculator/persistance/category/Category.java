package com.calculator.persistance.category;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.springframework.data.repository.cdi.Eager;

import java.util.List;

@Entity
@Table(name="categories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(targetEntity = Category.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "subcategories",
            joinColumns = { @JoinColumn(name = "subcategory_id") },
            inverseJoinColumns = { @JoinColumn(name = "parent_category_id") }
    )
    private List<Category> subcategories;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name="mcc_codes",
            joinColumns=@JoinColumn(name="category_id")
    )
    private List<Integer> mccCode;

    public Category(String name, List<Integer> mccCode) {
        this.name = name;
        this.mccCode = mccCode;
    }
}
