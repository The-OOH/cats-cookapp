package dev.cats.cookapp.services.impl;

import dev.cats.cookapp.dtos.request.recipe.RecipeIngredientRequest;
import dev.cats.cookapp.dtos.request.recipe.RecipeRequest;
import dev.cats.cookapp.dtos.response.recipe.RecipeInListResponse;
import dev.cats.cookapp.dtos.response.recipe.RecipeResponse;
import dev.cats.cookapp.mappers.RecipeMapper;
import dev.cats.cookapp.models.recipe.Product;
import dev.cats.cookapp.models.recipe.Recipe;
import dev.cats.cookapp.models.recipe.RecipeIngredient;
import dev.cats.cookapp.models.recipe.RecipeStep;
import dev.cats.cookapp.models.category.RecipeCategory;
import dev.cats.cookapp.models.unit.Unit;
import dev.cats.cookapp.repositories.CategoryRepository;
import dev.cats.cookapp.repositories.ProductRepository;
import dev.cats.cookapp.repositories.UnitRepository;
import dev.cats.cookapp.services.ClerkService;
import dev.cats.cookapp.services.RecipeAPIService;
import dev.cats.cookapp.services.RecipeService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RecipeAPIServiceImpl implements RecipeAPIService {
    RecipeService recipeService;
    CategoryRepository categoryRepository;
    UnitRepository unitRepository;
    ProductRepository productRepository;
    RecipeMapper recipeMapper;
    ClerkService clerkService;

    @Override
    public RecipeResponse getRecipe(Long id) {
        var recipe = recipeService.getRecipe(id);
        var recipeResponse = recipeMapper.toResponse(recipe);
        recipeResponse.setAuthor(clerkService.getUserDetailsById(recipe.getAuthorId()).orElse(null));
        return recipeResponse;
    }

    @Override
    public Page<RecipeInListResponse> getRecipesByUserId(String userId, Pageable pageable) {
        Page<Recipe> recipes = recipeService.findAllByAuthorId(userId, pageable);
        return recipes.map(recipeMapper::toInListResponse);
    }

    @Transactional
    public RecipeResponse saveRecipe(RecipeRequest dto, String currentUserId) {
        Recipe recipe = recipeMapper.toEntity(dto);
        List<RecipeIngredientRequest> reqs = dto.getIngredients();
        List<Long> productIds = reqs.stream()
                .map(RecipeIngredientRequest::getProductId)
                .distinct()
                .toList();
        List<Long> unitIds = reqs.stream()
                .map(r -> r.getMeasurements().getUnitId())
                .distinct()
                .toList();

        List<Product> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new IllegalArgumentException("One or more product IDs are invalid");
        }
        List<Unit> units = unitRepository.findAllById(unitIds);
        if (units.size() != unitIds.size()) {
            throw new IllegalArgumentException("One or more unit IDs are invalid");
        }

        Map<Long,Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));
        Map<Long,Unit> unitMap = units.stream()
                .collect(Collectors.toMap(Unit::getId, u -> u));

        Set<RecipeIngredient> ingredients = new HashSet<>();
        for (RecipeIngredientRequest req : reqs) {
            Product prod = productMap.get(req.getProductId());
            Unit unit = unitMap.get(req.getMeasurements().getUnitId());

            RecipeIngredient ri = new RecipeIngredient();
            ri.setRecipe(recipe);
            ri.setProduct(prod);
            ri.setUnit(unit);
            ri.setAmount(req.getMeasurements().getAmount());
            ingredients.add(ri);
        }
        recipe.setIngredients(ingredients);

        List<Long> catIds = dto.getCategories();
        List<RecipeCategory> cats = categoryRepository.findAllById(catIds);
        if (cats.size() != new HashSet<>(catIds).size()) {
            throw new IllegalArgumentException("One or more category IDs are invalid");
        }
        recipe.setCategories(new HashSet<>(cats));

        if (clerkService.getUserDetailsById(currentUserId).isPresent()) {
            recipe.setAuthorId(currentUserId);
        }

        Recipe saved = recipeService.saveRecipe(recipe);

        Set<RecipeStep> steps = recipe.getSteps();
        steps.forEach(step -> step.setRecipe(saved));
        saved.setSteps(steps);
        saved.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        var recipeResponse = recipeMapper.toResponse(recipeService.saveRecipe(saved));
        recipeResponse.setAuthor(clerkService.getUserDetailsById(recipe.getAuthorId()).orElse(null));
        return recipeResponse;
    }

    @Override
    public void deleteRecipe(Long id, String userId) {
        var recipe = recipeService.getRecipe(id);
        if (!recipe.getAuthorId().equals(userId)) {
            throw new RuntimeException("You can't delete this recipe");
        }
        recipeService.deleteRecipe(id);
    }
}
