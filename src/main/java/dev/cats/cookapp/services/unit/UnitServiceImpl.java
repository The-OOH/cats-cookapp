package dev.cats.cookapp.services.unit;

import dev.cats.cookapp.dto.response.UnitResponse;
import dev.cats.cookapp.mappers.UnitMapper;
import dev.cats.cookapp.repositories.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService{
    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    public List<UnitResponse> getUnits() {
        return unitRepository.findAll().stream()
                .map(unitMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UnitResponse> getImportantUnits() {
        return unitRepository.findAllByIsImportantTrue().stream()
                .map(unitMapper::toDto)
                .collect(Collectors.toList());
    }

}
