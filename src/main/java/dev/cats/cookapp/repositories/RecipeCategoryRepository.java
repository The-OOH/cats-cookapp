package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory, Long> {
    List<RecipeCategory> findAllByNameIn(List<String> names);
}