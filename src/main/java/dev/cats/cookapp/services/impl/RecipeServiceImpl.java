package dev.cats.cookapp.services.impl;

import dev.cats.cookapp.models.recipe.Recipe;
import dev.cats.cookapp.repositories.RecipeRepository;
import dev.cats.cookapp.services.RecipeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RecipeServiceImpl implements RecipeService {
    RecipeRepository recipeRepository;

    @Override
    public Recipe getRecipe(final Long id) {
        return this.recipeRepository.findById(id).orElseThrow();
    }

    @Override
    public Page<Recipe> findAllByAuthorId(final String userId, final Pageable pageable) {
        return this.recipeRepository.findAllByAuthorId(userId, pageable);
    }

    @Override
    public Recipe saveRecipe(final Recipe recipe) {
        return this.recipeRepository.save(recipe);
    }

    @Override
    public void deleteRecipe(final Long id) {
        this.recipeRepository.deleteById(id);
    }

}
