package dev.cats.cookapp.services;

import dev.cats.cookapp.dtos.response.recipe.ingredient.UnitResponse;

import java.util.List;

public interface UnitService {
    List<UnitResponse> getUnitsByName(String name);
}
