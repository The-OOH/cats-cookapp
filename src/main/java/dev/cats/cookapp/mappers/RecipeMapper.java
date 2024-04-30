package dev.cats.cookapp.mappers;

import dev.cats.cookapp.dto.response.RecipeListResponse;
import dev.cats.cookapp.dto.response.RecipeResponse;
import dev.cats.cookapp.models.Recipe;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = RecipeCategoryMapper.class)
public interface RecipeMapper{

    @Mapping(source = "readyInMinutes", target = "time")
    @Mapping(source = "pricePerServing", target = "price")
    RecipeResponse toDto(Recipe recipe);

    @Mapping(source = "readyInMinutes", target = "time")
    @Mapping(source = "pricePerServing", target = "price")
    RecipeListResponse toListDto(Recipe recipe);
}
