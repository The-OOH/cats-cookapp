package dev.cats.cookapp.mappers;

import dev.cats.cookapp.dtos.response.recipe.ingredient.UnitResponse;
import dev.cats.cookapp.models.unit.Unit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UnitMapper {
    @Mapping(target = "name.one", source = "name")
    @Mapping(target = "name.many", source = "abbreviation")
    @Mapping(target = "name.type", source = "type")
    UnitResponse toResponse(Unit unit);
}
