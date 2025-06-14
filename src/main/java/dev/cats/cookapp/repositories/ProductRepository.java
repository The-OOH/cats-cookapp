package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.recipe.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findTop10ByNameIgnoreCaseContaining(String namePart);

    List<Product> findAllByNameIgnoreCase(String namePart);

    @Query(value = """
            SELECT TRIM(LOWER(name)) AS dup_name
            FROM ingredients
            GROUP BY dup_name
            HAVING COUNT(*) > 1
            """, nativeQuery = true)
    List<String> findDuplicateNames();
}
