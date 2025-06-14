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
        this.clerkService = new StubClerkService();
        this.apiService = new RecipeAPIServiceImpl(
                this.recipeService,
                this.categoryRepository,
                this.unitRepository,
                this.productRepository,
                this.recipeMapper,
                this.clerkService
        );
    }

    @Test
    @DisplayName("getRecipe should map and attach author details")
    void getRecipe_mapsAndAttachesAuthor() {
        final Long id = 10L;
        final Recipe entity = new Recipe();
        entity.setAuthorId("usr123");
        final RecipeResponse mapped = new RecipeResponse();
        final UserDetails details = UserDetails.builder().id("usr123").name("Alice").build();

        when(this.recipeService.getRecipe(id)).thenReturn(entity);
        when(this.recipeMapper.toResponse(entity)).thenReturn(mapped);
        this.clerkService.setDetails(Optional.of(details));

        final RecipeResponse result = this.apiService.getRecipe(id);

        assertThat(result).isSameAs(mapped);
        assertThat(result.getAuthor()).isEqualTo(details);
        verify(this.recipeService).getRecipe(id);
        verify(this.recipeMapper).toResponse(entity);
    }

    @Test
    @DisplayName("saveRecipe should throw when product IDs invalid")
    void saveRecipe_invalidProducts_throws() {
        final RecipeRequest dto = new RecipeRequest();
        dto.setIngredients(Collections.singletonList(
                new RecipeIngredientRequest(1L, 5L, new MeasurementRequest(7L, 2.0))
        ));
        dto.setCategories(Collections.singletonList(1L));

        when(this.productRepository.findAllById(List.of(5L))).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> this.apiService.saveRecipe(dto, "userA"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("One or more product IDs are invalid");
    }

    @Test
    @DisplayName("saveRecipe should throw when unit IDs invalid")
    void saveRecipe_invalidUnits_throws() {
        final RecipeIngredientRequest req = new RecipeIngredientRequest(1L, 5L, new MeasurementRequest(7L, 2.0));
        final RecipeRequest dto = new RecipeRequest();
        dto.setIngredients(Collections.singletonList(req));
        dto.setCategories(Collections.singletonList(1L));

        when(this.productRepository.findAllById(List.of(5L))).thenReturn(List.of(new Product()));
        when(this.unitRepository.findAllById(List.of(7L))).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> this.apiService.saveRecipe(dto, "userB"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("One or more unit IDs are invalid");
    }

    @Test
    @DisplayName("saveRecipe should throw when category IDs invalid")
    void saveRecipe_invalidCategories_throws() {
        final RecipeRequest dto = new RecipeRequest();
        dto.setIngredients(Collections.singletonList(
                new RecipeIngredientRequest(1L, 1L, new MeasurementRequest(1L, 1.0))
        ));
        dto.setCategories(Arrays.asList(2L, 3L));

        final Recipe recipeEntity = new Recipe();
        when(this.recipeMapper.toEntity(dto)).thenReturn(recipeEntity);

        when(this.productRepository.findAllById(any())).thenReturn(List.of(new Product()));
        when(this.unitRepository.findAllById(any())).thenReturn(List.of(new Unit()));
        when(this.categoryRepository.findAllById(Arrays.asList(2L, 3L)))
                .thenReturn(Collections.singletonList(new RecipeCategory()));

        assertThatThrownBy(() -> this.apiService.saveRecipe(dto, "userC"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("One or more category IDs are invalid");
    }

    @Test
    @DisplayName("saveRecipe should persist and return response when valid")
    void saveRecipe_valid_persistsAndReturns() {
        final RecipeRequest dto = new RecipeRequest();
        final RecipeIngredientRequest ir = new RecipeIngredientRequest(1L, 1L, new MeasurementRequest(1L, 3.0));
        dto.setIngredients(Collections.singletonList(ir));
        dto.setCategories(Collections.singletonList(1L));
        dto.setSteps(Collections.emptyList());

        final Product p = new Product(); p.setId(1L);
        final Unit u = new Unit(); u.setId(1L);
        final RecipeCategory cat = new RecipeCategory(); cat.setId(1L);
        final Recipe entity = new Recipe(); entity.setSteps(Collections.emptySet());
        final Recipe savedEntity = new Recipe(); savedEntity.setSteps(Collections.emptySet());
        final RecipeResponse mapped = new RecipeResponse();
        final UserDetails details = UserDetails.builder().id("userX").name("Bob").build();

        when(this.productRepository.findAllById(List.of(1L))).thenReturn(List.of(p));
        when(this.unitRepository.findAllById(List.of(1L))).thenReturn(List.of(u));
        when(this.categoryRepository.findAllById(List.of(1L))).thenReturn(List.of(cat));
        when(this.recipeMapper.toEntity(dto)).thenReturn(entity);
        when(this.recipeService.saveRecipe(entity)).thenReturn(savedEntity);
        when(this.recipeService.saveRecipe(savedEntity)).thenReturn(savedEntity);
        when(this.recipeMapper.toResponse(savedEntity)).thenReturn(mapped);
        this.clerkService.setDetails(Optional.of(details));

        final RecipeResponse result = this.apiService.saveRecipe(dto, "userX");

        assertThat(result).isSameAs(mapped);
        assertThat(result.getAuthor()).isEqualTo(details);
        verify(this.productRepository).findAllById(List.of(1L));
        verify(this.unitRepository).findAllById(List.of(1L));
        verify(this.categoryRepository).findAllById(List.of(1L));
        verify(this.recipeService, times(2)).saveRecipe(any(Recipe.class));
        verify(this.recipeMapper).toResponse(savedEntity);
    }

    @Test
    @DisplayName("deleteRecipe should throw when user not author")
    void deleteRecipe_nonAuthor_throws() {
        final Recipe r = new Recipe();
        r.setAuthorId("owner");
        when(this.recipeService.getRecipe(9L)).thenReturn(r);

        assertThatThrownBy(() -> this.apiService.deleteRecipe(9L, "other"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("You can't delete this recipe");
    }

    @Test
    @DisplayName("deleteRecipe should delegate when user is author")
    void deleteRecipe_author_delegates() {
        final Recipe r = new Recipe(); r.setAuthorId("me");
        when(this.recipeService.getRecipe(8L)).thenReturn(r);

        this.apiService.deleteRecipe(8L, "me");
        verify(this.recipeService).deleteRecipe(8L);
    }

    /**
     * Stub for ClerkService to avoid Mockito inline-mock issues.
     */
    private static class StubClerkService extends ClerkService {
        private Optional<UserDetails> details = Optional.empty();
        @Override public Optional<UserDetails> getUserDetailsById(final String id) {
            return this.details;
        }
        public void setDetails(final Optional<UserDetails> details) {
            this.details = details;
        }
    }
}