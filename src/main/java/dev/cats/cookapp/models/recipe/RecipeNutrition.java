package dev.cats.cookapp.models.recipe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recipe_nutritions")
@Getter
@Setter
@NoArgsConstructor
public class RecipeNutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    @Column(nullable = false)
    private Double calories;

    @Basic(optional = false)
    @Column(nullable = false)
    private Double protein;

    @Basic(optional = false)
    @Column(nullable = false)
    private Double fat;

    @Basic(optional = false)
    @Column(nullable = false)
    private Double carbohydrate;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}
