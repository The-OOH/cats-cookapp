package dev.cats.cookapp.dtos.request.recipe;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StepRequest {
    private Long id;

    @NotNull
    private Integer stepNumber;

    @NotNull
    private String description;
}
