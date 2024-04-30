package dev.cats.cookapp.services.recipe;

import dev.cats.cookapp.dto.response.RecipeListResponse;
import dev.cats.cookapp.dto.response.RecipeResponse;
import org.springframework.data.domain.Page;

public interface RecipeService {
    Page<RecipeListResponse> getRecipes(int page, int size);
    RecipeResponse getRecipe(Long id);
}
