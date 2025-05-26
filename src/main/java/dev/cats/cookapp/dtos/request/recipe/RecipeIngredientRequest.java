package dev.cats.cookapp.dtos.request.recipe;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecipeIngredientRequest {
    private Long id;

    @NotNull
    private Long productId;

    @NotNull
    private MeasurementRequest measurements;
}
