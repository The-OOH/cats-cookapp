package dev.cats.cookapp.dtos.request.recipe;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecipeIngredientRequest {
    private Long id;

    @NotNull
    private Long productId;

    @NotNull
    private MeasurementRequest measurements;
}
