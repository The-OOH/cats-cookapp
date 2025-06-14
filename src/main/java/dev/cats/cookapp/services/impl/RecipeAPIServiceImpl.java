package dev.cats.cookapp.services.impl;

import dev.cats.cookapp.dtos.request.recipe.RecipeAIRequest;
import dev.cats.cookapp.dtos.request.recipe.RecipeIngredientRequest;
import dev.cats.cookapp.dtos.request.recipe.RecipeRequest;
import dev.cats.cookapp.dtos.response.PageResponse;
import dev.cats.cookapp.dtos.response.recipe.RecipeInListResponse;
import dev.cats.cookapp.dtos.response.recipe.RecipeResponse;
import dev.cats.cookapp.mappers.RecipeMapper;
import dev.cats.cookapp.models.recipe.*;
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
    public RecipeResponse getRecipe(final Long id) {
        final var recipe = this.recipeService.getRecipe(id);
        final var recipeResponse = this.recipeMapper.toResponse(recipe);
        recipeResponse.setAuthor(this.clerkService.getUserDetailsById(recipe.getAuthorId()).orElse(null));
        return recipeResponse;
    }

    @Override
    public PageResponse<RecipeInListResponse> getRecipesByUserId(final String userId, final Pageable pageable) {
        final Page<Recipe> recipes = this.recipeService.findAllByAuthorId(userId, pageable);
        return PageResponse.from(recipes.map(this.recipeMapper::toInListResponse));
    }

    @Transactional
    public RecipeResponse saveRecipe(final RecipeRequest dto, final String currentUserId) {
        dto.setSource(RecipeSource.MANUALLY_CREATED);
        final Recipe recipe = this.recipeMapper.toEntity(dto);
        final List<RecipeIngredientRequest> reqs = dto.getIngredients();
        final List<Long> productIds = reqs.stream()
                .map(RecipeIngredientRequest::getProductId)
                .distinct()
                .toList();
        final List<Long> unitIds = reqs.stream()
                .map(r -> r.getMeasurements().getUnitId())
                .distinct()
                .toList();

        final List<Product> products = this.productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new IllegalArgumentException("One or more product IDs are invalid");
        }
        final List<Unit> units = this.unitRepository.findAllById(unitIds);
        if (units.size() != unitIds.size()) {
            throw new IllegalArgumentException("One or more unit IDs are invalid");
        }

        final Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));
        final Map<Long, Unit> unitMap = units.stream()
                .collect(Collectors.toMap(Unit::getId, u -> u));

        final Set<RecipeIngredient> ingredients = new HashSet<>();
        for (final RecipeIngredientRequest req : reqs) {
            final Product prod = productMap.get(req.getProductId());
            final Unit unit = unitMap.get(req.getMeasurements().getUnitId());

            final RecipeIngredient ri = new RecipeIngredient();
            ri.setRecipe(recipe);
            ri.setProduct(prod);
            ri.setUnit(unit);
            ri.setAmount(req.getMeasurements().getAmount());
            ingredients.add(ri);
        }
        recipe.setIngredients(ingredients);

        final List<Long> catIds = dto.getCategories();
        final List<RecipeCategory> cats = this.categoryRepository.findAllById(catIds);
        if (cats.size() != new HashSet<>(catIds).size()) {
            throw new IllegalArgumentException("One or more category IDs are invalid");
        }
        recipe.setCategories(new HashSet<>(cats));

        if (this.clerkService.getUserDetailsById(currentUserId).isPresent()) {
            recipe.setAuthorId(currentUserId);
        }

        final Recipe saved = this.recipeService.saveRecipe(recipe);

        final Set<RecipeStep> steps = recipe.getSteps();
        steps.forEach(step -> step.setRecipe(saved));
        saved.setSteps(steps);
        saved.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        final var recipeResponse = this.recipeMapper.toResponse(this.recipeService.saveRecipe(saved));
        recipeResponse.setAuthor(this.clerkService.getUserDetailsById(recipe.getAuthorId()).orElse(null));
        return recipeResponse;
    }


    @Override
    @Transactional
    public RecipeResponse saveAiRecipe(final RecipeAIRequest recipeRequest, final String userId) {
        final var recipe = this.recipeMapper.fromAIRequest(recipeRequest);
        return this.saveRecipe(recipe, userId);
    }

    @Override
    public void deleteRecipe(final Long id, final String userId) {
        final var recipe = this.recipeService.getRecipe(id);
        if (!recipe.getAuthorId().equals(userId)) {
            throw new RuntimeException("You can't delete this recipe");
        }
        this.recipeService.deleteRecipe(id);
    }
}
