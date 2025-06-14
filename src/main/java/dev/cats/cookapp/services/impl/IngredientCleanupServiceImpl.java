package dev.cats.cookapp.services.impl;

import dev.cats.cookapp.models.recipe.Product;
import dev.cats.cookapp.repositories.ProductRepository;
import dev.cats.cookapp.repositories.RecipeIngredientRepository;
import dev.cats.cookapp.services.IngredientCleanupService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class IngredientCleanupServiceImpl implements IngredientCleanupService {
    ProductRepository productRepository;
    RecipeIngredientRepository recipeIngredientRepository;

    @Transactional
    public void collapseByName(final String rawName) {
        final String name = rawName.trim();
        final List<Product> dupList = this.productRepository.findAllByNameIgnoreCase(name);

        if (1 >= dupList.size()) {
            return;
        }

        final Product canonical = dupList.stream()
                .min(Comparator.comparing(Product::getId))
                .orElseThrow();
        final List<Long> duplicateIds = dupList.stream()
                .map(Product::getId)
                .filter(id -> !id.equals(canonical.getId()))
                .toList();

        this.recipeIngredientRepository.redirectIngredientIds(canonical.getId(), duplicateIds);
        this.productRepository.deleteAllByIdInBatch(duplicateIds);

        IngredientCleanupServiceImpl.log.info("Collapsed '{}' → kept id={}, removed {} rows",
                name, canonical.getId(), duplicateIds.size());
    }

    @Transactional
    public void collapseAllDuplicates() {
        final List<String> dupNames = this.productRepository.findDuplicateNames();

        dupNames.forEach(dupName -> {
            try {
                this.collapseByName(dupName);
            } catch (final Exception ex) {
                IngredientCleanupServiceImpl.log.error("Failed collapsing '{}': {}", dupName, ex.getMessage());
            }
        });

        IngredientCleanupServiceImpl.log.info("Finished duplicate-cleanup pass ({} names)", dupNames.size());
    }
}
