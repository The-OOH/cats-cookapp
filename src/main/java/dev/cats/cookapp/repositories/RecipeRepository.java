package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}