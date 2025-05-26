package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.collection.RecipesCollection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeCollectionRepository extends JpaRepository<RecipesCollection, Long> {
    @EntityGraph(attributePaths = {
            "collectionRecipes",
            "collectionRecipes.recipe",
            "collectionRecipes.recipe.categories",
            "collectionRecipes.recipe.nutrition",
    })
    List<RecipesCollection> findAllByUserId(String userId);

    @EntityGraph(attributePaths = {
            "collectionRecipes",
            "collectionRecipes.recipe",
            "collectionRecipes.recipe.categories",
            "collectionRecipes.recipe.nutrition",
            "collectionRecipes.recipe.categories.recipeCategoryType"
    })
    Optional<RecipesCollection> findByUserIdAndId(String userId, Long id);
}
