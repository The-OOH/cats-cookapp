package dev.cats.cookapp.mappers;

import dev.cats.cookapp.dtos.response.recipe.CategoryResponse;
import dev.cats.cookapp.models.category.RecipeCategory;
import dev.cats.cookapp.utils.StringConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "type", source = "recipeCategoryType.name")
    @Mapping(target = "slug", source = "name", qualifiedByName = "toKebabCase")
    CategoryResponse toResponse(RecipeCategory recipeCategory);

    @Named("toKebabCase")
    default String toKebabCase(String str) {
        return StringConverter.toKebabCase(str);
    }
}
