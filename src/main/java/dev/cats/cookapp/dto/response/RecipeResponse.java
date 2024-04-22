package dev.cats.cookapp.dto.response;

import lombok.Value;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link dev.cats.cookapp.models.Recipe}
 */
@Value
public class RecipeResponse implements Serializable {
    Long id;
    UserResponse created_by;
    Set<RecipeIngredientResponse> products;
    String title;
    Integer pricePerServing;
    Integer readyInMinutes;
    Integer servings;
    String image;
    String summary;
    List<RecipeCategoryResponse> categories;
    Integer healthScore;
    Double spoonacularScore;
}