package dev.cats.cookapp.mappers;

import dev.cats.cookapp.dtos.response.recipe.NutritionResponse;
import dev.cats.cookapp.models.recipe.RecipeNutrition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NutritionMapper {
    NutritionResponse toResponse(RecipeNutrition nutrition);
}
