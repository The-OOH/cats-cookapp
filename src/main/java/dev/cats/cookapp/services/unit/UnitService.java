package dev.cats.cookapp.services.unit;

import dev.cats.cookapp.dto.response.UnitResponse;

import java.util.List;

public interface UnitService {
    List<UnitResponse> getUnits();
}
