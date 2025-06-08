package dev.cats.cookapp.dtos.response.recipe;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipeInListResponse {
    Long id;
    String title;
    String difficulty;
    String slug;
    String mainImageUrl;
    Integer duration;
    Integer servings;
    List<CategoryResponse> categories;
    Double rating;
    Boolean isPublic;
}
