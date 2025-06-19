package dev.cats.cookapp.mappers;

import dev.cats.cookapp.dtos.request.recipe.RecipeIngredientRequest;
import dev.cats.cookapp.dtos.response.recipe.ingredient.IngredientResponse;
import dev.cats.cookapp.models.recipe.RecipeIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UnitMapper.class)
public interface IngredientMapper {
    @Mapping(target = "measurements.unit", source = "unit")
    @Mapping(target = "measurements.amount", source = "amount")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "productId", source = "product.id")
    IngredientResponse toResponse(RecipeIngredient ingredient);


    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "amount", source = "measurements.amount")
    @Mapping(target = "product", ignore = true)
    RecipeIngredient toEntity(RecipeIngredientRequest ingredientRequest);
}
