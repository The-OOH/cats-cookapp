package dev.cats.cookapp.mappers;

import dev.cats.cookapp.dto.response.RecipeCategoryResponse;
import dev.cats.cookapp.models.RecipeCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RecipeCategoryMapper {

    @Mapping(source = "recipeCategoryType.name", target = "recipeCategoryTypeName")
    RecipeCategoryResponse toDto(RecipeCategory recipeCategory);

}