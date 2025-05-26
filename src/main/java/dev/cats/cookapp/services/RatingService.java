package dev.cats.cookapp.services;

import dev.cats.cookapp.dtos.response.recipe.RecipeResponse;

public interface RatingService {

    RecipeResponse rateRecipe(Long recipeId, Double rating, String userId);

    RecipeResponse changeRating(Long recipeId, Double rating, String userId);

}
