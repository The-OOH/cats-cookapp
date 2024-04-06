package dev.cats.cookapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long recipe_id;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User created_by;

    @OneToMany(mappedBy = "recipe")
    private Set<RecipeIngredient> products = new HashSet<>();

    @Basic(optional = false)
    @Column(nullable = false)
    private Boolean vegetarian;

    @Basic(optional = false)
    @Column(nullable = false)
    private Boolean vegan;

    @Basic(optional = false)
    @Column(nullable = false)
    private Boolean glutenFree;

    @Basic(optional = false)
    @Column(nullable = false)
    private Boolean dairyFree;

    @Basic(optional = false)
    @Column(nullable = false, length = 128)
    private String title;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer pricePerServing;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer spoonacularId;

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

    @ManyToMany
    @JoinTable(name = "category_to_recipe",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_category_id"))
    private List<RecipeCategory> categories = new ArrayList<>();

    private Boolean cheap;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer healthScore;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer spoonacularScore;

}
