package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT r, (CASE WHEN ul.id IS NOT NULL THEN true ELSE false END) AS isInList " +
            "FROM Recipe r " +
            "LEFT JOIN r.lists ul ON ul.user.id = :userId " +
            "LEFT JOIN FETCH r.categories cat LEFT JOIN FETCH cat.recipeCategoryType " +
            "WHERE r.id IN :ids")
    List<Object[]> findAllByIdIn(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    @EntityGraph(attributePaths = {"createdBy", "products", "categories", "steps",  "lists",  "products.unit", "products.product",
            "categories.recipeCategoryType"})
    Optional<Recipe> findById(Long id);

    @Query("SELECT r.id FROM Recipe r")
    Page<Long> findAllIds(Pageable pageable);

    @Query("SELECT r.id FROM Recipe r JOIN r.categories c WHERE c.name IN :categoryNames")
    Page<Long> findAllByCategories(Pageable pageable, @Param("categoryNames") List<String> categoryNames);

    @Query("SELECT r.id FROM Recipe r WHERE r.createdBy.id = :userId")
    Page<Long> findAllIdsByUserId(Pageable pageable, @Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Recipe r WHERE r.createdAt BETWEEN :startDate AND :endDate")
    Integer countRecipesInSeason(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("SELECT COUNT(r) FROM Recipe r WHERE MONTH(r.createdAt) = :month")
    Integer countRecipesInMonth(@Param("month") Timestamp month);
}