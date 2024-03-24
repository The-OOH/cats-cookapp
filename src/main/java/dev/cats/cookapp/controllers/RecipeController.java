package dev.cats.cookapp.controllers;

import dev.cats.cookapp.dto.response.RecipeResponse;
import dev.cats.cookapp.services.recipe.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping
    public List<RecipeResponse> getRecipes() {
        return recipeService.getRecipes();
    }
}
