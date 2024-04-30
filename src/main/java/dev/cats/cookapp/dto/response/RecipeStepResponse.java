package dev.cats.cookapp.dto.response;

import lombok.Value;

@Value
public class RecipeStepResponse {
    Long id;
    String description;
}
