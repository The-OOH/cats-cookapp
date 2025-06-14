package dev.cats.cookapp.models.recipe;

import dev.cats.cookapp.models.category.RecipeCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    @Column(nullable = false)
    private String title;

    @Basic(optional = false)
    @Column(nullable = false)
    private String description;

    @Basic(optional = false)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RecipeSource source;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer durationTotal;

    @Basic(optional = false)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Basic(optional = false)
    @Column(nullable = false)
    private String mainImageUrl;

    @Basic(optional = false)
    @Column(nullable = false)
    private String authorId;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer servings;

    @Column(name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

    @Basic(optional = false)
    @Column(nullable = false)
    private Boolean isPublic = true;

    @Column
    private String externalSourceUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "recipe_categories",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<RecipeCategory> categories = new HashSet<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Set<RecipeStep> steps = new HashSet<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Set<RecipeIngredient> ingredients = new HashSet<>();

    @OneToOne(mappedBy = "recipe", cascade = CascadeType.ALL)
    private RecipeNutrition nutrition;

    @Formula(
            "(" +
                    "  SELECT CASE " +
                    "    WHEN COALESCE((SELECT SUM(e.count)   FROM external_recipe_rating e WHERE e.recipe_id = id),0)  " +
                    "     + COALESCE((SELECT COUNT(*)           FROM recipe_rating r WHERE r.recipe_id = id),0) = 0 " +
                    "    THEN NULL " +
                    "    ELSE ROUND( " +
                    "      (" +
                    "        COALESCE((SELECT SUM(e.rating * e.count) FROM external_recipe_rating e WHERE e.recipe_id = id),0)" +
                    "      + COALESCE((SELECT SUM(r.rating) FROM recipe_rating r WHERE r.recipe_id = id),0)" +
                    "      )::numeric " +
                    "      / " +
                    "      (" +
                    "        COALESCE((SELECT SUM(e.count) FROM external_recipe_rating e WHERE e.recipe_id = id),0)" +
                    "      + COALESCE((SELECT COUNT(*)   FROM recipe_rating r WHERE r.recipe_id = id),0)" +
                    "      ), 2" +
                    "    ) " +
                    "  END" +
                    ")"
    )
    private Double finalRating;

    @Column(name = "popularity_score", nullable = false)
    private Double popularityScore = 0.0;
}
