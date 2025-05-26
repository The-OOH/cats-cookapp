package dev.cats.cookapp.services;

import dev.cats.cookapp.models.recipe.Recipe;

public interface RecipeService {
    Recipe getRecipe(Long id);

    Recipe saveRecipe(Recipe recipeRequest);

    void deleteRecipe(Long id);
}
