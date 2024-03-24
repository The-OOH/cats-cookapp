package dev.cats.cookapp.mappers;

import dev.cats.cookapp.dto.response.RecipeResponse;
import dev.cats.cookapp.models.Recipe;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = RecipeCategoryMapper.class)
public interface RecipeMapper{

    RecipeResponse toDto(Recipe recipe);
}
