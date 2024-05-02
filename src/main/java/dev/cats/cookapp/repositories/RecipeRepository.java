package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT r, (CASE WHEN ul.id IS NOT NULL THEN true ELSE false END) AS isInList " +
            "FROM Recipe r " +
            "LEFT JOIN r.lists ul ON ul.user.id = :userId " +
            "LEFT JOIN FETCH r.categories cat " +
            "WHERE r.id IN :ids")
    List<Object[]> findAllByIdIn(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    @EntityGraph(attributePaths = {"createdBy", "products", "categories", "steps",  "lists",  "products.unit", "products.product",
            "categories.recipeCategoryType"})
    Optional<Recipe> findById(Long id);

    @Query("SELECT r.id FROM Recipe r")
    Page<Long> findAllIds(Pageable pageable);
}