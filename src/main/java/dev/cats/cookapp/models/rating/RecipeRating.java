package dev.cats.cookapp.models.rating;

import dev.cats.cookapp.models.recipe.Recipe;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "recipe_rating")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "rating", nullable = false)
    private Double rating;

    @Column(name="created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;
}
