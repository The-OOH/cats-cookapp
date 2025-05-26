package dev.cats.cookapp.dtos.response.collection;

import dev.cats.cookapp.dtos.response.recipe.RecipeInListResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class FullCollectionResponse {
    Long id;

    @NotNull
    String userId;

    @NotNull
    String name;

    @NotNull
    Integer recipeCount;

    @NotNull
    List<RecipeInListResponse> recipes;
}
