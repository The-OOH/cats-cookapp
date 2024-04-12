package dev.cats.cookapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "aisle", nullable = false)
    private String aisle;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "product")
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();

}