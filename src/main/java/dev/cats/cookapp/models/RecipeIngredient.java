package dev.cats.cookapp.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
/*
* TABLE recipe_products {
  recipe_product_id INT [pk, unique, increment]
  recipe_id INT [ref: > recipes.recipe_id]
  product_id INT [ref: > products.product_id]

  original text // original name with units included (probably)
  amount double
  unit_id int
}*/
@Getter
@Setter
@Entity
@Table(name = "recipe_ingredient")
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "original", nullable = false)
    private String original;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "recipe_recipe_id")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}