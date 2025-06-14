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
    public RecipeResponse rateRecipe(final Long recipeId, final Double rating, final String userId) {
        final var recipe = this.recipeService.getRecipe(recipeId);
        if (this.ratingRepository.findByRecipeIdAndUserId(recipeId, userId).isPresent()) {
            throw new IllegalArgumentException("You already rated this recipe");
        }
        if (1 < rating || 0 > rating) {
            throw new IllegalArgumentException("Rating must be between 0 and 1");
        }
        this.ratingRepository.save(RecipeRating.builder()
                .recipe(recipe)
                .userId(userId)
                .rating(rating).build());

        this.entityManager.flush(); // To update the recipe rating by formula
        this.entityManager.refresh(recipe);

        return this.recipeApiService.getRecipe(recipeId);
    }

    @Transactional
    public RecipeResponse changeRating(final Long recipeId, final Double rating, final String userId) {
        if (1 < rating || 0 > rating) {
            throw new IllegalArgumentException("Rating must be between 0 and 1");
        }
        final var oldRating = this.ratingRepository.findByRecipeIdAndUserId(recipeId, userId).orElseThrow();
        oldRating.setRating(rating);
        this.ratingRepository.save(oldRating);

        this.entityManager.flush();
        this.entityManager.refresh(oldRating.getRecipe());

        return this.recipeApiService.getRecipe(recipeId);
    }
}
