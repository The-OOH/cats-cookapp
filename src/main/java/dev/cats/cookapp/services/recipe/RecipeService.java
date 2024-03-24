package dev.cats.cookapp.services.recipe;

import dev.cats.cookapp.dto.response.RecipeResponse;

import java.util.List;

public interface RecipeService {
    List<RecipeResponse> getRecipes();
}
