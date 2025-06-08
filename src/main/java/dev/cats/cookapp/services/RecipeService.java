package dev.cats.cookapp.services;

import dev.cats.cookapp.models.recipe.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeService {
    Recipe getRecipe(Long id);

    Page<Recipe> findAllByAuthorId(String userId, Pageable pageable);

    Recipe saveRecipe(Recipe recipeRequest);

    void deleteRecipe(Long id);
}
