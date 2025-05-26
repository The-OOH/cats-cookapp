package dev.cats.cookapp.mappers;

import dev.cats.cookapp.dtos.request.recipe.StepRequest;
import dev.cats.cookapp.dtos.response.recipe.StepResponse;
import dev.cats.cookapp.models.recipe.RecipeStep;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StepMapper {

    StepResponse toResponse(RecipeStep step);

    RecipeStep toEntity(StepRequest stepRequest);
}
