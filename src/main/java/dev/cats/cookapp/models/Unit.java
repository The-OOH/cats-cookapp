package dev.cats.cookapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "units")
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "is_metric")
    private Boolean isMetric = true;

    @Column(name = "is_important")
    private Boolean isImportant = false;

    @OneToMany(mappedBy = "unit")
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();

}