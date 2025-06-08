package dev.cats.cookapp.controllers;

import dev.cats.cookapp.dtos.request.recipe.RecipeRequest;
import dev.cats.cookapp.dtos.response.PageResponse;
import dev.cats.cookapp.dtos.response.recipe.RecipeInListResponse;
import dev.cats.cookapp.dtos.response.recipe.RecipeResponse;
import dev.cats.cookapp.services.RecipeAPIService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
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

    @GetMapping("/my")
    public PageResponse<RecipeInListResponse> getMyRecipes(@RequestHeader("x-user-id") String userId,
                                                           @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                           @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return recipeAPIService.getRecipesByUserId(userId, PageRequest.of(page - 1, size));
    }

    @PostMapping
    public RecipeResponse createRecipe(@RequestBody RecipeRequest recipeRequest, @RequestHeader("x-user-id") String userId) {
        return recipeAPIService.saveRecipe(recipeRequest, userId);
    }

    @PutMapping("/{id}")
    public RecipeResponse updateRecipe(@RequestBody RecipeRequest recipeRequest, @PathVariable("id") Long id, @RequestHeader("x-user-id") String userId) {
        if (recipeRequest.getId() == null) {
            throw new IllegalArgumentException("Recipe ID must be provided");
        }
        else {
            recipeRequest.setId(id);
        }
        return recipeAPIService.saveRecipe(recipeRequest, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable("id") Long id, @RequestHeader("x-user-id") String userId) {
        recipeAPIService.deleteRecipe(id, userId);
    }
}
