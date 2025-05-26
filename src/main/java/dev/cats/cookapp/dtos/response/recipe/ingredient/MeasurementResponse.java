package dev.cats.cookapp.dtos.response.recipe.ingredient;

import lombok.Data;

@Data
public class MeasurementResponse {
    private UnitResponse unit;
    private Double amount;
}
