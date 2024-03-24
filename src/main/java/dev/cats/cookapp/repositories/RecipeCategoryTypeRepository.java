package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.RecipeCategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeCategoryTypeRepository extends JpaRepository<RecipeCategoryType, Long> {
}