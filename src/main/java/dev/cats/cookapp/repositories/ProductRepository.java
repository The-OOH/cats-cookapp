package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.recipe.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
