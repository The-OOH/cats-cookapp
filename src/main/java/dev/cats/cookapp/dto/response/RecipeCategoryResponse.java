package dev.cats.cookapp.dto.response;

import dev.cats.cookapp.models.RecipeCategory;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO for {@link dev.cats.cookapp.models.RecipeCategory}
 */
@Value
public class RecipeCategoryResponse implements Serializable {
    Long id;
    String name;
    String image;
    String type;

    public static Set<RecipeCategoryResponse> from(Set<RecipeCategory> categories) {
        return categories.stream()
                .map(category -> new RecipeCategoryResponse(category.getId(),
                        category.getName(), category.getImage(), category.getRecipeCategoryType().getName()))
                .collect(Collectors.toSet());
    }
}