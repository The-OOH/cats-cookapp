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
        final String query = "cup";
        final Unit u1 = new Unit(); u1.setId(1L); u1.setName("cup"); u1.setAbbreviation("c"); u1.setType(UnitMeasurementType.imperial);
        final UnitResponse r1 = new UnitResponse(1L, new NameResponse("cup","cups",UnitMeasurementType.imperial));

        when(this.unitRepository.findTop10ByNameIgnoreCaseContaining(query)).thenReturn(List.of(u1));
        when(this.unitMapper.toResponse(u1)).thenReturn(r1);

        // Act
        final List<UnitResponse> result = this.unitService.getUnitsByName(query);

        // Assert
        verify(this.unitRepository).findTop10ByNameIgnoreCaseContaining(query);
        verify(this.unitRepository, never()).findAll();
        verify(this.unitMapper).toResponse(u1);
        assertThat(result).containsExactly(r1);
    }

    @Test
    @DisplayName("getUnitsByName should fallback to all when no matches found")
    void getUnitsByName_emptyMatches_returnsAllMapped() {
        // Arrange
        final String query = "xyz";
        final Unit u2 = new Unit(); u2.setId(2L); u2.setName("teaspoon"); u2.setAbbreviation("tsp"); u2.setType(UnitMeasurementType.metric);
        final Unit u3 = new Unit(); u3.setId(3L); u3.setName("tablespoon"); u3.setAbbreviation("tbsp"); u3.setType(UnitMeasurementType.metric);
        final UnitResponse r2 = new UnitResponse(2L, new NameResponse("teaspoon","teaspoons",UnitMeasurementType.metric));
        final UnitResponse r3 = new UnitResponse(3L, new NameResponse("tablespoon","tablespoons",UnitMeasurementType.metric));

        when(this.unitRepository.findTop10ByNameIgnoreCaseContaining(query)).thenReturn(Collections.emptyList());
        when(this.unitRepository.findAll()).thenReturn(List.of(u2, u3));
        when(this.unitMapper.toResponse(u2)).thenReturn(r2);
        when(this.unitMapper.toResponse(u3)).thenReturn(r3);

        // Act
        final List<UnitResponse> result = this.unitService.getUnitsByName(query);

        // Assert
        verify(this.unitRepository).findTop10ByNameIgnoreCaseContaining(query);
        verify(this.unitRepository).findAll();
        verify(this.unitMapper).toResponse(u2);
        verify(this.unitMapper).toResponse(u3);
        assertThat(result).containsExactly(r2, r3);
    }
}
