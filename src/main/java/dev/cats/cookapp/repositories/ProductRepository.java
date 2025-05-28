package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.recipe.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findTop10ByNameIgnoreCaseContaining(String namePart);
}
