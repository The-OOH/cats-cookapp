package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory, Long> {
}