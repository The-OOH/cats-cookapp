package dev.cats.cookapp.services.recipe;

import dev.cats.cookapp.dto.response.RecipeResponse;
import org.springframework.data.domain.Page;

public interface RecipeService {
    Page<RecipeResponse> getRecipes(int page, int size);
}
