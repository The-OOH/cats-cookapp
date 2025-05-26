package dev.cats.cookapp.dtos.response.recipe;

import dev.cats.cookapp.dtos.UserDetails;
import dev.cats.cookapp.dtos.response.recipe.ingredient.IngredientResponse;
import dev.cats.cookapp.models.recipe.RecipeSource;
import lombok.Data;

import java.util.List;

@Data
public class RecipeResponse {
    private Long id;
    private String title;
    private String difficulty;
    private String slug;
    private String mainImageUrl;
    private String description;
    private RecipeSource source;
    private UserDetails author;
    private Integer duration;
    private Integer servings;
    private NutritionResponse nutritions;
    private List<CategoryResponse> categories;
    private List<IngredientResponse> ingredients;
    private List<StepResponse> steps;
    private Boolean isPublic;
    private String sourceUrl;
    private Double rating;
}
