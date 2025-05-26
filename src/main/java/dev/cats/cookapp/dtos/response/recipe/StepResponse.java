package dev.cats.cookapp.dtos.response.recipe;

import lombok.Data;

@Data
public class StepResponse {
    Long id;
    String description;
    Integer stepNumber;
}
