package dev.cats.cookapp.services.impl;

import dev.cats.cookapp.dtos.response.recipe.ingredient.NameResponse;
import dev.cats.cookapp.dtos.response.recipe.ingredient.UnitResponse;
import dev.cats.cookapp.mappers.UnitMapper;
import dev.cats.cookapp.models.unit.Unit;
import dev.cats.cookapp.models.unit.UnitMeasurementType;
import dev.cats.cookapp.repositories.UnitRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnitServiceImplTest {

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private UnitMapper unitMapper;

    @InjectMocks
    private UnitServiceImpl unitService;

    @Test
    @DisplayName("getUnitsByName should return mapped top matches when found")
    void getUnitsByName_nonEmptyMatches_returnsMapped() {
        // Arrange
        String query = "cup";
        Unit u1 = new Unit(); u1.setId(1L); u1.setName("cup"); u1.setAbbreviation("c"); u1.setType(UnitMeasurementType.imperial);
        UnitResponse r1 = new UnitResponse(1L, new NameResponse("cup","cups",UnitMeasurementType.imperial));

        when(unitRepository.findTop10ByNameIgnoreCaseContaining(query)).thenReturn(List.of(u1));
        when(unitMapper.toResponse(u1)).thenReturn(r1);

        // Act
        List<UnitResponse> result = unitService.getUnitsByName(query);

        // Assert
        verify(unitRepository).findTop10ByNameIgnoreCaseContaining(query);
        verify(unitRepository, never()).findAll();
        verify(unitMapper).toResponse(u1);
        assertThat(result).containsExactly(r1);
    }

    @Test
    @DisplayName("getUnitsByName should fallback to all when no matches found")
    void getUnitsByName_emptyMatches_returnsAllMapped() {
        // Arrange
        String query = "xyz";
        Unit u2 = new Unit(); u2.setId(2L); u2.setName("teaspoon"); u2.setAbbreviation("tsp"); u2.setType(UnitMeasurementType.metric);
        Unit u3 = new Unit(); u3.setId(3L); u3.setName("tablespoon"); u3.setAbbreviation("tbsp"); u3.setType(UnitMeasurementType.metric);
        UnitResponse r2 = new UnitResponse(2L, new NameResponse("teaspoon","teaspoons",UnitMeasurementType.metric));
        UnitResponse r3 = new UnitResponse(3L, new NameResponse("tablespoon","tablespoons",UnitMeasurementType.metric));

        when(unitRepository.findTop10ByNameIgnoreCaseContaining(query)).thenReturn(Collections.emptyList());
        when(unitRepository.findAll()).thenReturn(List.of(u2, u3));
        when(unitMapper.toResponse(u2)).thenReturn(r2);
        when(unitMapper.toResponse(u3)).thenReturn(r3);

        // Act
        List<UnitResponse> result = unitService.getUnitsByName(query);

        // Assert
        verify(unitRepository).findTop10ByNameIgnoreCaseContaining(query);
        verify(unitRepository).findAll();
        verify(unitMapper).toResponse(u2);
        verify(unitMapper).toResponse(u3);
        assertThat(result).containsExactly(r2, r3);
    }
}
