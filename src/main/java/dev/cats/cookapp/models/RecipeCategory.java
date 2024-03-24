package dev.cats.cookapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "recipe_category")
public class RecipeCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 128, unique = true)
    private String name;

    @ManyToMany
    private Set<Recipe> recipes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "recipe_category_type_id")
    private RecipeCategoryType recipeCategoryType;

}