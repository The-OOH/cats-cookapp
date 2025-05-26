package dev.cats.cookapp.models;

import dev.cats.cookapp.models.recipe.Recipe;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "user_cooking_stats")
@Getter
@Setter
public class UserCookingStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    @Column(nullable = false)
    private String userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @Column(name = "cooked_date")
    private Timestamp cookedAt;
}
