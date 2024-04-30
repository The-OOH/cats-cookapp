package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @EntityGraph(attributePaths = {"created_by", "products", "categories", "products.unit", "products.product",
            "categories.recipeCategoryType"})
    List<Recipe> findAllByIdIn(List<Long> ids);

    @EntityGraph(attributePaths = {"created_by", "products", "categories", "products.unit", "products.product",
            "categories.recipeCategoryType" , "steps"})
    Optional<Recipe> findById(Long id);

    @Query("SELECT r.id FROM Recipe r")
    Page<Long> findAllIds(Pageable pageable);
}