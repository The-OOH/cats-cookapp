package dev.cats.cookapp.controllers;

import dev.cats.cookapp.dto.response.RecipeListResponse;
import dev.cats.cookapp.dto.response.RecipeResponse;
import dev.cats.cookapp.services.recipe.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping
    public Page<RecipeListResponse> getRecipes(@RequestParam(required = false, defaultValue = "0") int page,
                                               @RequestParam(required = false, defaultValue = "100") int size) {
        return recipeService.getRecipes(page, size);
    }

    @GetMapping("/{id}")
    public RecipeResponse getRecipe(@PathVariable Long id) {
        return recipeService.getRecipe(id);
    }
}
