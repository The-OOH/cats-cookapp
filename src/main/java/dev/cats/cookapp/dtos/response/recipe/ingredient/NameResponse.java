package dev.cats.cookapp.dtos.response.recipe.ingredient;

import dev.cats.cookapp.models.unit.UnitMeasurementType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NameResponse {
    private String one;
    private String many;
    private UnitMeasurementType type;
}
