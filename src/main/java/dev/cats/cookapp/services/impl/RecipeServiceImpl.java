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
    public Recipe getRecipe(Long id) {
        return recipeRepository.findById(id).orElseThrow();
    }

    @Override
    public Page<Recipe> findAllByAuthorId(String userId, Pageable pageable) {
        return recipeRepository.findAllByAuthorId(userId, pageable);
    }

    @Override
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

}
