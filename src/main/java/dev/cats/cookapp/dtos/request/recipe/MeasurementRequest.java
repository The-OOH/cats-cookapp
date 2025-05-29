package dev.cats.cookapp.dtos.request.recipe;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MeasurementRequest {
    @NotNull
    private Long unitId;

    @NotNull
    private Double amount;
}
