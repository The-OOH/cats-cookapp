package dev.cats.cookapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User created_by;

    @OneToMany(mappedBy = "recipe")
    private Set<RecipeIngredient> products = new HashSet<>();

    @Basic(optional = false)
    @Column(nullable = false, length = 128)
    private String title;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer pricePerServing;

    @Basic(optional = false)
    @Column(nullable = false)
    private Long spoonacularId;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer readyInMinutes;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer servings;

    @Basic(optional = false)
    @Column(nullable = false, length = 128)
    private String image;

    @Basic(optional = false)
    @Column(nullable = false, length = 512)
    private String summary;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "category_to_recipe",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<RecipeCategory> categories = new HashSet<>();

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer healthScore;

    @Basic(optional = false)
    @Column(nullable = false)
    private Double spoonacularScore;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe")
    private List<RecipeStep> steps = new ArrayList<>();

}
