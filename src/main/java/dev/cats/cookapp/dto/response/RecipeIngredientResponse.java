package dev.cats.cookapp.dto.response;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link dev.cats.cookapp.models.RecipeIngredient}
 */
@Value
public class RecipeIngredientResponse implements Serializable {
    Long id;
    String original;
    Double amount;
    UnitResponse unit;
    ProductResponse product;
}