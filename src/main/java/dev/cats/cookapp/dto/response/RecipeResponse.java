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
    Long recipe_id;
    UserResponse created_by;
    Set<RecipeIngredientResponse> products;
    Boolean vegetarian;
    Boolean vegan;
    Boolean glutenFree;
    Boolean dairyFree;
    String title;
    Integer pricePerServing;
    Integer spoonacularId;
    Integer readyInMinutes;
    Integer servings;
    String image;
    String summary;
    List<RecipeCategoryResponse> categories;
    Boolean cheap;
    Integer healthScore;
    Integer spoonacularScore;
}