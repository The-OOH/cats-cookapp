package dev.cats.cookapp.dtos.request.recipe;

import dev.cats.cookapp.models.recipe.RecipeSource;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RecipeAIRequest {
    @NotNull
    private String title;

    @NotNull
    private String difficulty;

    private String description;

    private RecipeSource source  = RecipeSource.AI;

    @NotNull
    private Integer duration;

    @NotNull
    private Integer servings;

    private List<Long> categories;

    @NotNull
    private List<RecipeIngredientRequest> ingredients;

    @NotNull
    private List<StepRequest> steps;

    private Boolean isPublic = false;
}
