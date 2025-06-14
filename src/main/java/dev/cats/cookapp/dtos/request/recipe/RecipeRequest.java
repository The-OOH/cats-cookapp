package dev.cats.cookapp.dtos.request.recipe;

import dev.cats.cookapp.models.recipe.RecipeSource;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RecipeRequest {
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String difficulty;

    @NotNull
    private String mainImageUrl;

    @NotNull
    private String description;

    private RecipeSource source;

    @NotNull
    private Integer duration;

    @NotNull
    private Integer servings;

    @NotNull
    private List<Long> categories;

    @NotNull
    private List<RecipeIngredientRequest> ingredients;

    @NotNull
    private List<StepRequest> steps;

    private Boolean isPublic = true;
}
