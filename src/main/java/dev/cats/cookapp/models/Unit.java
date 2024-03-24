package dev.cats.cookapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/*
*
Table units {
  unit_id INT [pk, unique, increment]
  name text
  isMetric boolean
}*/
@Getter
@Setter
@Entity
@Table(name = "unit")
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "is_metric", nullable = false)
    private Boolean isMetric;

    @OneToMany(mappedBy = "unit")
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();

}