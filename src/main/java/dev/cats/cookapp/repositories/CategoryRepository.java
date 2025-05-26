package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.category.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<RecipeCategory, Long> {
}
