package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.category.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CategoryRepository extends JpaRepository<RecipeCategory, Long> {
    List<RecipeCategory> findByNameIgnoreCaseContaining(String name);
}
