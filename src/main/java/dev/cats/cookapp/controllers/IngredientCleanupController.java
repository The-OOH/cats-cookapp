package dev.cats.cookapp.controllers;

import dev.cats.cookapp.services.IngredientCleanupService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cleanup")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class IngredientCleanupController {
    IngredientCleanupService ingredientCleanupService;

    @RequestMapping("/collapse-by-name")
    public void collapseByName(@RequestParam final String name) {
        this.ingredientCleanupService.collapseByName(name);
    }

    @RequestMapping("/collapse-all")
    public void collapseAll() {
        this.ingredientCleanupService.collapseAllDuplicates();
    }
}
