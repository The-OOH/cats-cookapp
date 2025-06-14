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
    public RecipeResponse getRecipe(@PathVariable("id") final Long id) {
        return this.recipeAPIService.getRecipe(id);
    }

    @GetMapping("/my")
    public PageResponse<RecipeInListResponse> getMyRecipes(@RequestHeader("x-user-id") final String userId,
                                                           @RequestParam(name = "page", defaultValue = "1") final Integer page,
                                                           @RequestParam(name = "size", defaultValue = "10") final Integer size) {
        return this.recipeAPIService.getRecipesByUserId(userId, PageRequest.of(page - 1, size));
    }

    @PostMapping
    public RecipeResponse createRecipe(@RequestBody final RecipeRequest recipeRequest, @RequestHeader("x-user-id") final String userId) {
        return this.recipeAPIService.saveRecipe(recipeRequest, userId);
    }

    @PutMapping("/{id}")
    public RecipeResponse updateRecipe(@RequestBody final RecipeRequest recipeRequest, @PathVariable("id") final Long id, @RequestHeader("x-user-id") final String userId) {
        if (null == recipeRequest.getId()) {
            throw new IllegalArgumentException("Recipe ID must be provided");
        } else {
            recipeRequest.setId(id);
        }
        return this.recipeAPIService.saveRecipe(recipeRequest, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable("id") final Long id, @RequestHeader("x-user-id") final String userId) {
        this.recipeAPIService.deleteRecipe(id, userId);
    }
}
