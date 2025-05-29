package dev.cats.cookapp.services.impl;

import dev.cats.cookapp.dtos.request.recipe.MeasurementRequest;
import dev.cats.cookapp.dtos.request.recipe.RecipeIngredientRequest;
import dev.cats.cookapp.dtos.request.recipe.RecipeRequest;
import dev.cats.cookapp.dtos.response.recipe.RecipeResponse;
import dev.cats.cookapp.dtos.UserDetails;
import dev.cats.cookapp.mappers.RecipeMapper;
import dev.cats.cookapp.models.category.RecipeCategory;
import dev.cats.cookapp.models.recipe.Product;
import dev.cats.cookapp.models.recipe.Recipe;
import dev.cats.cookapp.models.unit.Unit;
import dev.cats.cookapp.repositories.CategoryRepository;
import dev.cats.cookapp.repositories.ProductRepository;
import dev.cats.cookapp.repositories.UnitRepository;
import dev.cats.cookapp.services.ClerkService;
import dev.cats.cookapp.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeAPIServiceImplTest {

    @Mock private RecipeService recipeService;
    @Mock private CategoryRepository categoryRepository;
    @Mock private UnitRepository unitRepository;
    @Mock private ProductRepository productRepository;
    @Mock private RecipeMapper recipeMapper;

    private StubClerkService clerkService;
    private RecipeAPIServiceImpl apiService;

    @BeforeEach
    void setUp() {
        clerkService = new StubClerkService();
        apiService = new RecipeAPIServiceImpl(
                recipeService,
                categoryRepository,
                unitRepository,
                productRepository,
                recipeMapper,
                clerkService
        );
    }

    @Test
    @DisplayName("getRecipe should map and attach author details")
    void getRecipe_mapsAndAttachesAuthor() {
        Long id = 10L;
        Recipe entity = new Recipe();
        entity.setAuthorId("usr123");
        RecipeResponse mapped = new RecipeResponse();
        UserDetails details = UserDetails.builder().id("usr123").name("Alice").build();

        when(recipeService.getRecipe(id)).thenReturn(entity);
        when(recipeMapper.toResponse(entity)).thenReturn(mapped);
        clerkService.setDetails(Optional.of(details));

        RecipeResponse result = apiService.getRecipe(id);

        assertThat(result).isSameAs(mapped);
        assertThat(result.getAuthor()).isEqualTo(details);
        verify(recipeService).getRecipe(id);
        verify(recipeMapper).toResponse(entity);
    }

    @Test
    @DisplayName("saveRecipe should throw when product IDs invalid")
    void saveRecipe_invalidProducts_throws() {
        RecipeRequest dto = new RecipeRequest();
        dto.setIngredients(Collections.singletonList(
                new RecipeIngredientRequest(1L, 5L, new MeasurementRequest(7L, 2.0))
        ));
        dto.setCategories(Collections.singletonList(1L));

        when(productRepository.findAllById(List.of(5L))).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> apiService.saveRecipe(dto, "userA"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("One or more product IDs are invalid");
    }

    @Test
    @DisplayName("saveRecipe should throw when unit IDs invalid")
    void saveRecipe_invalidUnits_throws() {
        RecipeIngredientRequest req = new RecipeIngredientRequest(1L, 5L, new MeasurementRequest(7L, 2.0));
        RecipeRequest dto = new RecipeRequest();
        dto.setIngredients(Collections.singletonList(req));
        dto.setCategories(Collections.singletonList(1L));

        when(productRepository.findAllById(List.of(5L))).thenReturn(List.of(new Product()));
        when(unitRepository.findAllById(List.of(7L))).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> apiService.saveRecipe(dto, "userB"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("One or more unit IDs are invalid");
    }

    @Test
    @DisplayName("saveRecipe should throw when category IDs invalid")
    void saveRecipe_invalidCategories_throws() {
        RecipeRequest dto = new RecipeRequest();
        dto.setIngredients(Collections.singletonList(
                new RecipeIngredientRequest(1L, 1L, new MeasurementRequest(1L, 1.0))
        ));
        dto.setCategories(Arrays.asList(2L, 3L));

        Recipe recipeEntity = new Recipe();
        when(recipeMapper.toEntity(dto)).thenReturn(recipeEntity);

        when(productRepository.findAllById(any())).thenReturn(List.of(new Product()));
        when(unitRepository.findAllById(any())).thenReturn(List.of(new Unit()));
        when(categoryRepository.findAllById(Arrays.asList(2L, 3L)))
                .thenReturn(Collections.singletonList(new RecipeCategory()));

        assertThatThrownBy(() -> apiService.saveRecipe(dto, "userC"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("One or more category IDs are invalid");
    }

    @Test
    @DisplayName("saveRecipe should persist and return response when valid")
    void saveRecipe_valid_persistsAndReturns() {
        RecipeRequest dto = new RecipeRequest();
        RecipeIngredientRequest ir = new RecipeIngredientRequest(1L, 1L, new MeasurementRequest(1L, 3.0));
        dto.setIngredients(Collections.singletonList(ir));
        dto.setCategories(Collections.singletonList(1L));
        dto.setSteps(Collections.emptyList());

        Product p = new Product(); p.setId(1L);
        Unit u = new Unit(); u.setId(1L);
        RecipeCategory cat = new RecipeCategory(); cat.setId(1L);
        Recipe entity = new Recipe(); entity.setSteps(Collections.emptySet());
        Recipe savedEntity = new Recipe(); savedEntity.setSteps(Collections.emptySet());
        RecipeResponse mapped = new RecipeResponse();
        UserDetails details = UserDetails.builder().id("userX").name("Bob").build();

        when(productRepository.findAllById(List.of(1L))).thenReturn(List.of(p));
        when(unitRepository.findAllById(List.of(1L))).thenReturn(List.of(u));
        when(categoryRepository.findAllById(List.of(1L))).thenReturn(List.of(cat));
        when(recipeMapper.toEntity(dto)).thenReturn(entity);
        when(recipeService.saveRecipe(entity)).thenReturn(savedEntity);
        when(recipeService.saveRecipe(savedEntity)).thenReturn(savedEntity);
        when(recipeMapper.toResponse(savedEntity)).thenReturn(mapped);
        clerkService.setDetails(Optional.of(details));

        RecipeResponse result = apiService.saveRecipe(dto, "userX");

        assertThat(result).isSameAs(mapped);
        assertThat(result.getAuthor()).isEqualTo(details);
        verify(productRepository).findAllById(List.of(1L));
        verify(unitRepository).findAllById(List.of(1L));
        verify(categoryRepository).findAllById(List.of(1L));
        verify(recipeService, times(2)).saveRecipe(any(Recipe.class));
        verify(recipeMapper).toResponse(savedEntity);
    }

    @Test
    @DisplayName("deleteRecipe should throw when user not author")
    void deleteRecipe_nonAuthor_throws() {
        Recipe r = new Recipe();
        r.setAuthorId("owner");
        when(recipeService.getRecipe(9L)).thenReturn(r);

        assertThatThrownBy(() -> apiService.deleteRecipe(9L, "other"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("You can't delete this recipe");
    }

    @Test
    @DisplayName("deleteRecipe should delegate when user is author")
    void deleteRecipe_author_delegates() {
        Recipe r = new Recipe(); r.setAuthorId("me");
        when(recipeService.getRecipe(8L)).thenReturn(r);

        apiService.deleteRecipe(8L, "me");
        verify(recipeService).deleteRecipe(8L);
    }

    /**
     * Stub for ClerkService to avoid Mockito inline-mock issues.
     */
    private static class StubClerkService extends ClerkService {
        private Optional<UserDetails> details = Optional.empty();
        @Override public Optional<UserDetails> getUserDetailsById(String id) {
            return details;
        }
        public void setDetails(Optional<UserDetails> details) {
            this.details = details;
        }
    }
}