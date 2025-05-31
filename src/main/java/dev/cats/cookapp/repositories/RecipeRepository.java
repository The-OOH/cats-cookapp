package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.recipe.Recipe;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @EntityGraph(attributePaths = {"categories", "steps", "ingredients", "nutrition"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Recipe> findById(@NotNull Long id);
}
