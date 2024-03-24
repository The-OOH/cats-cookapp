package dev.cats.cookapp.controllers;

import dev.cats.cookapp.dto.response.UnitResponse;
import dev.cats.cookapp.services.unit.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/units")
@RequiredArgsConstructor
public class UnitController {
    private final UnitService unitService;

    @GetMapping
    public List<UnitResponse> getUnits(){
        return unitService.getUnits();
    }
}
