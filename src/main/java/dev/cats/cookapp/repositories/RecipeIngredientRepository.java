package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.recipe.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    @Modifying
    @Query(value = """
        UPDATE recipe_ingredients
        SET ingredient_id = :canonicalId
        WHERE ingredient_id IN (:duplicateIds)
        """, nativeQuery = true)
    void redirectIngredientIds(@Param("canonicalId") Long canonicalId,
                               @Param("duplicateIds") List<Long> duplicateIds);
}
