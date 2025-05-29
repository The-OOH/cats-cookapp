package dev.cats.cookapp.dtos.response.recipe.ingredient;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnitResponse {
    private Long id;
    private NameResponse name;
}
