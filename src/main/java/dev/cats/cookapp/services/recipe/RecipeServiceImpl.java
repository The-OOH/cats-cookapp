package dev.cats.cookapp.services.recipe;

import dev.cats.cookapp.dto.request.RecipeProductRequest;
import dev.cats.cookapp.dto.request.RecipeRequest;
import dev.cats.cookapp.dto.response.RecipeListResponse;
import dev.cats.cookapp.dto.response.RecipeResponse;
import dev.cats.cookapp.mappers.RecipeMapper;
import dev.cats.cookapp.models.*;
import dev.cats.cookapp.repositories.*;
import dev.cats.cookapp.services.TokenExtractService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeCategoryRepository recipeCategoryRepository;
    private final RecipeMapper recipeMapper;
    private final TokenExtractService tokenExtractService;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final UnitRepository unitRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final ProductRepository productRepository;

    @Override
    public Page<RecipeListResponse> getRecipes(int page, int size, Long userId) {
        var pageRequest = PageRequest.of(page, size);
        var pageOfIds = recipeRepository.findAllIds(pageRequest);

        return getRecipes(pageOfIds, userId);
    }

    @Override
    public Page<RecipeListResponse> getMyRecipes(int page, int size, Long userId) {
        var pageOfIds = recipeRepository.findAllIdsByUserId(PageRequest.of(page, size), userId);

        return getRecipes(pageOfIds, userId);
    }

    private Page<RecipeListResponse> getRecipes(Page<Long> pageOfIds, Long userId){
        var recipes = recipeRepository.findAllByIdIn(pageOfIds.getContent(), userId);
        Set<RecipeListResponse> recipeListResponses = recipes.stream()
                .map(objects -> {
                    var recipe = (dev.cats.cookapp.models.Recipe) objects[0];
                    var isSaved = (Boolean) objects[1];
                    Set<RecipeCategory> categories = recipe.getCategories();
                    return new RecipeListResponse(recipe.getId(), recipe.getTitle(), recipe.getPricePerServing(),
                            recipe.getReadyInMinutes(), recipe.getServings(), recipe.getImage(),
                            isSaved,recipe.getCalories(), categories);
                })
                .collect(Collectors.toSet());
        return new PageImpl<>(recipeListResponses.stream().toList(),
                pageOfIds.getPageable(), pageOfIds.getTotalElements());
    }

    @Override
    public RecipeResponse getRecipe(Long id) {
        var recipe = recipeRepository.findById(id).orElseThrow();
        var recipeResult = recipeMapper.toDto(recipe);
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = tokenExtractService.extractToken(auth).orElseThrow();
        var isSavedForUser = recipe.getLists().stream().anyMatch(userList ->
            userList.getUser().getId().equals(user.getId()));
        recipeResult.setIsSaved(isSavedForUser);
        return recipeResult;
    }

    @Override
    public RecipeResponse addRecipe(RecipeRequest recipe) {
        var categories = recipeCategoryRepository.findAllByNameIn(recipe.getCategories());
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = tokenExtractService.extractToken(auth).orElseThrow();
        var recipeEntity = recipeMapper.toEntity(recipe);
        recipeEntity.setCategories(new HashSet<>(categories));
        recipeEntity.setCreatedBy(user);
        recipeEntity.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        return getRecipeResponse(recipe, recipeEntity);
    }

    @Override
    public RecipeResponse updateRecipe(Long id, RecipeRequest recipe) {
        var categories = recipeCategoryRepository.findAllByNameIn(recipe.getCategories());
        var recipeEntity = recipeRepository.findById(id).orElseThrow();
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = tokenExtractService.extractToken(auth).orElseThrow();
        if(recipeEntity.getCreatedBy() == null) {
            recipeEntity.setCreatedBy(user);
        }
        if(!recipeEntity.getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("You can't update this recipe");
        }
        recipeEntity.setTitle(recipe.getTitle());
        recipeEntity.setPricePerServing(recipe.getPrice());
        recipeEntity.setReadyInMinutes(recipe.getTime());
        recipeEntity.setServings(recipe.getServings());
        recipeEntity.setImage(recipe.getImage());
        recipeEntity.setCategories(new HashSet<>(categories));
        return getRecipeResponse(recipe, recipeEntity);
    }

    private RecipeResponse getRecipeResponse(RecipeRequest recipe, Recipe recipeEntity) {
        recipeRepository.save(recipeEntity);

        var recipeSteps = saveSteps(recipe.getSteps(), recipeEntity);
        recipeStepRepository.saveAll(recipeSteps);
        recipeEntity.setSteps(recipeSteps);

        var recipeIngredients = recipe.getProducts();
        List<RecipeIngredient> recipeIngredientEntities = saveIngredients(recipeIngredients, recipeEntity);
        recipeIngredientRepository.saveAll(recipeIngredientEntities);
        recipeEntity.setProducts(new HashSet<>(recipeIngredientEntities));

        return recipeMapper.toDto(recipeEntity);
    }

    private List<RecipeStep> saveSteps(List<String> steps, Recipe recipe) {
        return steps.stream()
                .map(step -> {
                    var stepEntity = new RecipeStep();
                    stepEntity.setDescription(step);
                    stepEntity.setRecipe(recipe);
                    return stepEntity;
                })
                .toList();
    }

    private List<RecipeIngredient> saveIngredients(List<RecipeProductRequest> products, Recipe recipe) {
        return products.stream()
                .map(ingredient -> {
                    var ingredientEntity = new RecipeIngredient();
                    ingredientEntity.setAmount(ingredient.getAmount());
                    var product = new Product();
                    if(ingredient.getProductId() != null) {
                        product = productRepository.findById(ingredient.getProductId()).orElseThrow();
                    } else {
                        product.setName(ingredient.getCustomName());
                        productRepository.save(product);
                    }
                    ingredientEntity.setProduct(product);
                    ingredientEntity.setUnit(unitRepository.findById(ingredient.getUnitId()).orElseThrow());
                    ingredientEntity.setRecipe(recipe);
                    return ingredientEntity;
                })
                .toList();
    }
}
