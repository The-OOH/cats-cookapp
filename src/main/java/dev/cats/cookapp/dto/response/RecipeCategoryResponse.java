package dev.cats.cookapp.dto.response;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link dev.cats.cookapp.models.RecipeCategory}
 */
@Value
public class RecipeCategoryResponse implements Serializable {
    Long id;
    String name;
    String recipeCategoryTypeName;
}