package dev.cats.cookapp.models.rating;

import dev.cats.cookapp.models.recipe.Recipe;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "external_recipe_rating")
@Getter
public class ExternalRecipeRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @Column(name = "external_source_url", nullable = false)
    private String externalSourceUrl;

    @Column(name = "rating", nullable = false)
    private Double rating;

    @Column(name = "count", nullable = false)
    private Integer count;
}
