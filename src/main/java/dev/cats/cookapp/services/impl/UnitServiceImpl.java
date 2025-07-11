package dev.cats.cookapp.services.impl;

import dev.cats.cookapp.dtos.response.recipe.ingredient.UnitResponse;
import dev.cats.cookapp.mappers.UnitMapper;
import dev.cats.cookapp.repositories.UnitRepository;
import dev.cats.cookapp.services.UnitService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UnitServiceImpl implements UnitService {
    UnitRepository unitRepository;
    UnitMapper unitMapper;

    @Override
    public List<UnitResponse> findAll() {
        return this.unitRepository.findAll().stream().map(this.unitMapper::toResponse).toList();
    }

    @Override
    public List<UnitResponse> getUnitsByName(final String name) {
        var matches = this.unitRepository.findTop10ByNameIgnoreCaseContaining(name);
        if (matches.isEmpty()) {
            matches = this.unitRepository.findAll();
        }
        return matches.stream().map(this.unitMapper::toResponse).toList();
    }
}
