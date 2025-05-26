package dev.cats.cookapp.dtos.response.recipe.ingredient;

import lombok.Data;

@Data
public class UnitResponse {
    private Long id;
    private NameResponse name;
}
