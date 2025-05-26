package dev.cats.cookapp.mappers;

import dev.cats.cookapp.dtos.response.recipe.RecipeCollectionResponse;
import dev.cats.cookapp.dtos.response.recipe.RecipeInListResponse;
import dev.cats.cookapp.models.recipe.Recipe;
import dev.cats.cookapp.dtos.request.recipe.RecipeRequest;
import dev.cats.cookapp.dtos.response.recipe.RecipeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, IngredientMapper.class, StepMapper.class, UnitMapper.class, NutritionMapper.class})
public interface RecipeMapper {
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "duration", source = "durationTotal")
    @Mapping(target = "nutritions", source = "nutrition")
    @Mapping(target = "sourceUrl", source = "externalSourceUrl")
    @Mapping(target = "rating", source = "finalRating")
    @Mapping(target = "slug", source = "title", qualifiedByName = "toKebabCase")
    RecipeResponse toResponse(Recipe recipe);

    @Mapping(target = "externalSourceUrl", ignore = true)
    @Mapping(target = "finalRating", ignore = true)
    @Mapping(target = "durationTotal", source = "duration")
    @Mapping(target = "nutrition", ignore = true)
    @Mapping(target = "ingredients", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Recipe toEntity(RecipeRequest recipeRequest);

    RecipeCollectionResponse toCollectionResponse(Recipe recipe);

    @Mapping(target = "duration", source = "durationTotal")
    @Mapping(target = "rating", source = "finalRating")
    @Mapping(target = "slug", source = "title", qualifiedByName = "toKebabCase")
    RecipeInListResponse toInListResponse(Recipe recipe);
}
