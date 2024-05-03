package dev.cats.cookapp.services.recipe;

import dev.cats.cookapp.dto.request.RecipeRequest;
import dev.cats.cookapp.dto.response.RecipeListResponse;
import dev.cats.cookapp.dto.response.RecipeResponse;
import org.springframework.data.domain.Page;

public interface RecipeService {
    Page<RecipeListResponse> getRecipes(int page, int size, Long userId);
    Page<RecipeListResponse> getMyRecipes(int page, int size, Long userId);
    RecipeResponse getRecipe(Long id);
    RecipeResponse addRecipe(RecipeRequest recipe);
    RecipeResponse updateRecipe(Long id, RecipeRequest recipe);
}
