package dev.cats.cookapp.services.impl;

import dev.cats.cookapp.dtos.response.recipe.RecipeResponse;
import dev.cats.cookapp.models.rating.RecipeRating;
import dev.cats.cookapp.repositories.RatingRepository;
import dev.cats.cookapp.services.RatingService;
import dev.cats.cookapp.services.RecipeAPIService;
import dev.cats.cookapp.services.RecipeService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RatingServiceImpl implements RatingService {
    RecipeService recipeService;
    RecipeAPIService recipeApiService;
    RatingRepository ratingRepository;
    EntityManager entityManager;

    @Transactional
    public RecipeResponse rateRecipe(Long recipeId, Double rating, String userId) {
        var recipe = recipeService.getRecipe(recipeId);
        if (ratingRepository.findByRecipeIdAndUserId(recipeId, userId).isPresent()) {
            throw new IllegalArgumentException("You already rated this recipe");
        }
        if (rating > 1 || rating < 0) {
            throw new IllegalArgumentException("Rating must be between 0 and 1");
        }
        ratingRepository.save(RecipeRating.builder()
                .recipe(recipe)
                .userId(userId)
                .rating(rating).build());

        entityManager.flush(); // To update the recipe rating by formula
        entityManager.refresh(recipe);

        return recipeApiService.getRecipe(recipeId);
    }

    @Transactional
    public RecipeResponse changeRating(Long recipeId, Double rating, String userId) {
        if (rating > 1 || rating < 0) {
            throw new IllegalArgumentException("Rating must be between 0 and 1");
        }
        var oldRating = ratingRepository.findByRecipeIdAndUserId(recipeId, userId).orElseThrow();
        oldRating.setRating(rating);
        ratingRepository.save(oldRating);

        entityManager.flush();
        entityManager.refresh(oldRating.getRecipe());

        return recipeApiService.getRecipe(recipeId);
    }
}
