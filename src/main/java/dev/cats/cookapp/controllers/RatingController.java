package dev.cats.cookapp.controllers;

import dev.cats.cookapp.dtos.response.recipe.RecipeResponse;
import dev.cats.cookapp.services.RatingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipes/{recipeId}/rating")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RatingController {
    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<RecipeResponse> rateRecipe(@PathVariable("recipeId") Long recipeId,
                                                     @RequestHeader("x-user-id") String userId,
                                                     @RequestParam("rating") Double rating) {
        return ResponseEntity.ok(ratingService.rateRecipe(recipeId, rating, userId));
    }

    @PutMapping
    public ResponseEntity<RecipeResponse> changeRating(@PathVariable("recipeId") Long recipeId,
                                                       @RequestHeader("x-user-id") String userId,
                                                      @RequestParam("rating") Double rating) {
        return ResponseEntity.ok(ratingService.changeRating(recipeId, rating, userId));
    }
}
