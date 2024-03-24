package dev.cats.cookapp.mappers;

import dev.cats.cookapp.dto.response.UnitResponse;
import dev.cats.cookapp.models.Unit;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UnitMapper {
    UnitResponse toDto(Unit unit);
}
