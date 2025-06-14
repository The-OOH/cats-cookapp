package dev.cats.cookapp.controllers;

import dev.cats.cookapp.dtos.response.recipe.ingredient.UnitResponse;
import dev.cats.cookapp.services.UnitService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/units")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UnitController {
    UnitService unitService;

    @GetMapping
    public ResponseEntity<List<UnitResponse>> getUnitsByName(
            @RequestParam(name = "name") final String name) {
        return ResponseEntity.ok(this.unitService.getUnitsByName(name));
    }
}
