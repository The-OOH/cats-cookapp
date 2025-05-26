package dev.cats.cookapp.services;

import dev.cats.cookapp.dtos.request.recipe.RecipeRequest;
import dev.cats.cookapp.dtos.response.recipe.RecipeResponse;

public interface RecipeAPIService {
    RecipeResponse getRecipe(Long id);

    RecipeResponse saveRecipe(RecipeRequest recipeRequest, String userId);

    void deleteRecipe(Long id, String userId);
}
