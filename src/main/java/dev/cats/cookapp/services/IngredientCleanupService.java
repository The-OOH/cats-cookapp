package dev.cats.cookapp.services;

public interface IngredientCleanupService {
    void collapseByName(String rawName);

    void collapseAllDuplicates();

}
