package dev.cats.cookapp.controllers;

import dev.cats.cookapp.dto.request.RecipeRequest;
import dev.cats.cookapp.dto.response.RecipeListResponse;
import dev.cats.cookapp.dto.response.RecipeResponse;
import dev.cats.cookapp.models.User;
import dev.cats.cookapp.services.TokenExtractService;
import dev.cats.cookapp.services.recipe.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;
    private final TokenExtractService tokenExtractService;

    @GetMapping
    public Page<RecipeListResponse> getRecipes(@RequestParam(required = false, defaultValue = "0") int page,
                                               @RequestParam(required = false, defaultValue = "100") int size) {
        var isPresent = tokenExtractService.extractToken(SecurityContextHolder.getContext().getAuthentication())
                .isPresent();
        if(isPresent) {
            User user = tokenExtractService.extractToken(SecurityContextHolder.getContext().getAuthentication()).get();
            return recipeService.getRecipes(page, size, user.getId());
        }
        return recipeService.getRecipes(page, size, (long) -1);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getRecipe(@PathVariable Long id) {
        return ResponseEntity.ok().body(recipeService.getRecipe(id));
    }

    

    @PostMapping
    public ResponseEntity<RecipeResponse> addRecipe(@RequestBody RecipeRequest recipe){
        return ResponseEntity.ok().body(recipeService.addRecipe(recipe));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponse> updateRecipe(@PathVariable Long id, @RequestBody RecipeRequest recipe){
        return ResponseEntity.ok().body(recipeService.updateRecipe(id, recipe));
    }
}
