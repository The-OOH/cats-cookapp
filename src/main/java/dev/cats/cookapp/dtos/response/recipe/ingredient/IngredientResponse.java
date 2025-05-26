package dev.cats.cookapp.dtos.response.recipe.ingredient;

import lombok.Data;

@Data
public class IngredientResponse {
    private Long id;
    private String name;
    private MeasurementResponse measurements;
}
