package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.rating.RecipeRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<RecipeRating, Long> {
    Optional<RecipeRating> findByRecipeIdAndUserId(Long recipeId, String userId);
}
