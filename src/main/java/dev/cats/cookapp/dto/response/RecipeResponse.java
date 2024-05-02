package dev.cats.cookapp.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link dev.cats.cookapp.models.Recipe}
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RecipeResponse implements Serializable {
    Long id;
    UserResponse created_by;
    Set<RecipeIngredientResponse> products;
    String title;
    Integer price;
    Integer time;
    Integer servings;
    String image;
    List<RecipeCategoryResponse> categories;
    Boolean isSaved;
    Integer calories;
    List<RecipeStepResponse> steps;
}