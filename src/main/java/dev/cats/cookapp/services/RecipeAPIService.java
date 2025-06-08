package dev.cats.cookapp.services;

import dev.cats.cookapp.dtos.request.recipe.RecipeRequest;
import dev.cats.cookapp.dtos.response.recipe.RecipeInListResponse;
import dev.cats.cookapp.dtos.response.recipe.RecipeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeAPIService {
    RecipeResponse getRecipe(Long id);

    Page<RecipeInListResponse> getRecipesByUserId(String userId, Pageable pageable);

    RecipeResponse saveRecipe(RecipeRequest recipeRequest, String userId);

    void deleteRecipe(Long id, String userId);
}
