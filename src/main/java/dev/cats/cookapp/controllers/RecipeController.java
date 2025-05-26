package dev.cats.cookapp.controllers;

import dev.cats.cookapp.dtos.request.recipe.RecipeRequest;
import dev.cats.cookapp.dtos.response.recipe.RecipeResponse;
import dev.cats.cookapp.services.RecipeAPIService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RecipeController {
    RecipeAPIService recipeAPIService;

    @GetMapping("/{id}")
    public RecipeResponse getRecipe(@PathVariable("id") Long id) {
        return recipeAPIService.getRecipe(id);
    }

    @PostMapping("/{userId}")
    public RecipeResponse createRecipe(@RequestBody RecipeRequest recipeRequest, @PathVariable("userId") String userId) {
        return recipeAPIService.saveRecipe(recipeRequest, userId);
    }

    @PutMapping("/{id}/{userId}")
    public RecipeResponse updateRecipe(@RequestBody RecipeRequest recipeRequest, @PathVariable("id") Long id, @PathVariable("userId") String userId) {
        if (recipeRequest.getId() == null) {
            throw new IllegalArgumentException("Recipe ID must be provided");
        }
        else {
            recipeRequest.setId(id);
        }
        return recipeAPIService.saveRecipe(recipeRequest, userId);
    }

    @DeleteMapping("/{id}/{userId}")
    public void deleteRecipe(@PathVariable("id") Long id, @PathVariable("userId") String userId) {
        recipeAPIService.deleteRecipe(id, userId);
    }
}
