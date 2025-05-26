package dev.cats.cookapp.dtos.response.recipe;

import lombok.Data;

import java.util.List;

@Data
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
}
