package dev.cats.cookapp.dto.response;

import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link dev.cats.cookapp.models.Recipe}
 */
@Value
public class RecipeListResponse implements Serializable {
    Long id;
    String title;
    Integer price;
    Integer time;
    Integer servings;
    String image;
    List<RecipeCategoryResponse> categories;
}