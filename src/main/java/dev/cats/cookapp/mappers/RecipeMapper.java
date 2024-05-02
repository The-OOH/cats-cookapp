package dev.cats.cookapp.mappers;

import dev.cats.cookapp.dto.request.RecipeRequest;
import dev.cats.cookapp.dto.response.RecipeListResponse;
import dev.cats.cookapp.dto.response.RecipeResponse;
import dev.cats.cookapp.models.Recipe;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = RecipeCategoryMapper.class)
public interface RecipeMapper {

    @Mapping(source = "readyInMinutes", target = "time")
    @Mapping(source = "pricePerServing", target = "price")
    @Mapping(source = "createdBy", target = "created_by")
    RecipeResponse toDto(Recipe recipe);

    @Mapping(source = "readyInMinutes", target = "time")
    @Mapping(source = "pricePerServing", target = "price")
    RecipeListResponse toListDto(Recipe recipe);

    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "steps", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "readyInMinutes", source = "time")
    @Mapping(target = "pricePerServing", source = "price")
    Recipe toEntity(RecipeRequest recipeRequest);

    Recipe toEntity(RecipeListResponse recipeListResponse);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Recipe partialUpdate(RecipeListResponse recipeListResponse, @MappingTarget Recipe recipe);
}
