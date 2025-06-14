package dev.cats.cookapp.services.impl;

import dev.cats.cookapp.dtos.response.recipe.RecipeResponse;
import dev.cats.cookapp.models.rating.RecipeRating;
import dev.cats.cookapp.models.recipe.Recipe;
import dev.cats.cookapp.repositories.RatingRepository;
import dev.cats.cookapp.services.RecipeAPIService;
import dev.cats.cookapp.services.RecipeService;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @Mock
    private RecipeService recipeService;

    @Mock
    private RecipeAPIService recipeApiService;

    @Mock
    private RatingRepository ratingRepository;

    private static class StubEntityManager implements EntityManager {
        private boolean flushed;
        private boolean refreshed;

        @Override
        public void persist(final Object o) {

        }

        @Override
        public <T> T merge(final T t) {
            return null;
        }

        @Override
        public void remove(final Object o) {

        }

        @Override
        public <T> T find(final Class<T> aClass, final Object o) {
            return null;
        }

        @Override
        public <T> T find(final Class<T> aClass, final Object o, final Map<String, Object> map) {
            return null;
        }

        @Override
        public <T> T find(final Class<T> aClass, final Object o, final LockModeType lockModeType) {
            return null;
        }

        @Override
        public <T> T find(final Class<T> aClass, final Object o, final LockModeType lockModeType, final Map<String, Object> map) {
            return null;
        }

        @Override
        public <T> T getReference(final Class<T> aClass, final Object o) {
            return null;
        }

        @Override
        public void flush() {
            this.flushed = true;
        }

        @Override
        public void setFlushMode(final FlushModeType flushModeType) {

        }

        @Override
        public FlushModeType getFlushMode() {
            return null;
        }

        @Override
        public void lock(final Object o, final LockModeType lockModeType) {

        }

        @Override
        public void lock(final Object o, final LockModeType lockModeType, final Map<String, Object> map) {

        }

        @Override
        public void refresh(final Object entity) {
            this.refreshed = true;
        }

        @Override
        public void refresh(final Object o, final Map<String, Object> map) {

        }

        @Override
        public void refresh(final Object o, final LockModeType lockModeType) {

        }

        @Override
        public void refresh(final Object o, final LockModeType lockModeType, final Map<String, Object> map) {

        }

        @Override
        public void clear() {

        }

        @Override
        public void detach(final Object o) {

        }

        @Override
        public boolean contains(final Object o) {
            return false;
        }

        @Override
        public LockModeType getLockMode(final Object o) {
            return null;
        }

        @Override
        public void setProperty(final String s, final Object o) {

        }

        @Override
        public Map<String, Object> getProperties() {
            return Map.of();
        }

        @Override
        public Query createQuery(final String s) {
            return null;
        }

        @Override
        public <T> TypedQuery<T> createQuery(final CriteriaQuery<T> criteriaQuery) {
            return null;
        }

        @Override
        public Query createQuery(final CriteriaUpdate criteriaUpdate) {
            return null;
        }

        @Override
        public Query createQuery(final CriteriaDelete criteriaDelete) {
            return null;
        }

        @Override
        public <T> TypedQuery<T> createQuery(final String s, final Class<T> aClass) {
            return null;
        }

        @Override
        public Query createNamedQuery(final String s) {
            return null;
        }

        @Override
        public <T> TypedQuery<T> createNamedQuery(final String s, final Class<T> aClass) {
            return null;
        }

        @Override
        public Query createNativeQuery(final String s) {
            return null;
        }

        @Override
        public Query createNativeQuery(final String s, final Class aClass) {
            return null;
        }

        @Override
        public Query createNativeQuery(final String s, final String s1) {
            return null;
        }

        @Override
        public StoredProcedureQuery createNamedStoredProcedureQuery(final String s) {
            return null;
        }

        @Override
        public StoredProcedureQuery createStoredProcedureQuery(final String s) {
            return null;
        }

        @Override
        public StoredProcedureQuery createStoredProcedureQuery(final String s, final Class... classes) {
            return null;
        }

        @Override
        public StoredProcedureQuery createStoredProcedureQuery(final String s, final String... strings) {
            return null;
        }

        @Override
        public void joinTransaction() {

        }

        @Override
        public boolean isJoinedToTransaction() {
            return false;
        }

        @Override
        public <T> T unwrap(final Class<T> aClass) {
            return null;
        }

        @Override
        public Object getDelegate() {
            return null;
        }

        @Override
        public void close() {

        }

        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public EntityTransaction getTransaction() {
            return null;
        }

        @Override
        public EntityManagerFactory getEntityManagerFactory() {
            return null;
        }

        @Override
        public CriteriaBuilder getCriteriaBuilder() {
            return null;
        }

        @Override
        public Metamodel getMetamodel() {
            return null;
        }

        @Override
        public <T> EntityGraph<T> createEntityGraph(final Class<T> aClass) {
            return null;
        }

        @Override
        public EntityGraph<?> createEntityGraph(final String s) {
            return null;
        }

        @Override
        public EntityGraph<?> getEntityGraph(final String s) {
            return null;
        }

        @Override
        public <T> List<EntityGraph<? super T>> getEntityGraphs(final Class<T> aClass) {
            return List.of();
        }

        // add getters
        public boolean isFlushed() {
            return this.flushed;
        }

        public boolean isRefreshed() {
            return this.refreshed;
        }

    }

    private StubEntityManager entityManager;
    private RatingServiceImpl ratingService;

    @BeforeEach
    void setUp() {
        this.entityManager = new StubEntityManager();
        this.ratingService = new RatingServiceImpl(this.recipeService, this.recipeApiService, this.ratingRepository, this.entityManager);
    }

    @Test
    @DisplayName("rateRecipe should save new rating and return updated recipe response when valid and not previously rated")
    void rateRecipe_validInput_savesRatingAndReturnsResponse() {
        final Long recipeId = 1L;
        final Double ratingValue = 0.75;
        final String userId = "user1";
        final Recipe recipe = new Recipe();
        final RecipeResponse response = new RecipeResponse();

        when(this.recipeService.getRecipe(recipeId)).thenReturn(recipe);
        when(this.ratingRepository.findByRecipeIdAndUserId(recipeId, userId)).thenReturn(Optional.empty());
        when(this.recipeApiService.getRecipe(recipeId)).thenReturn(response);

        final RecipeResponse result = this.ratingService.rateRecipe(recipeId, ratingValue, userId);

        final ArgumentCaptor<RecipeRating> captor = ArgumentCaptor.forClass(RecipeRating.class);
        verify(this.ratingRepository).save(captor.capture());
        final RecipeRating saved = captor.getValue();
        assertThat(saved.getRecipe()).isSameAs(recipe);
        assertThat(saved.getRating()).isEqualTo(ratingValue);
        assertThat(saved.getUserId()).isEqualTo(userId);

        assertThat(this.entityManager.isFlushed()).isTrue();
        assertThat(this.entityManager.isRefreshed()).isTrue();

        verify(this.recipeApiService).getRecipe(recipeId);
        assertThat(result).isSameAs(response);
    }

    @Test
    @DisplayName("rateRecipe should throw when already rated")
    void rateRecipe_alreadyRated_throws() {
        final Long recipeId = 2L;
        final String userId = "user2";

        when(this.recipeService.getRecipe(recipeId)).thenReturn(new Recipe());
        when(this.ratingRepository.findByRecipeIdAndUserId(recipeId, userId)).thenReturn(Optional.of(new RecipeRating()));

        assertThatThrownBy(() -> this.ratingService.rateRecipe(recipeId, 0.5, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("You already rated this recipe");
    }

    @Test
    @DisplayName("rateRecipe should throw when rating out of bounds")
    void rateRecipe_invalidRating_throws() {
        final Long recipeId = 3L;
        final String userId = "user3";

        when(this.recipeService.getRecipe(recipeId)).thenReturn(new Recipe());
        when(this.ratingRepository.findByRecipeIdAndUserId(recipeId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.ratingService.rateRecipe(recipeId, 1.2, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Rating must be between 0 and 1");

        assertThatThrownBy(() -> this.ratingService.rateRecipe(recipeId, -0.1, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Rating must be between 0 and 1");
    }

    @Test
    @DisplayName("changeRating should update and return response when valid")
    void changeRating_validInput_updatesAndReturns() {
        final Long recipeId = 4L;
        final String userId = "user4";
        final Double newRating = 0.4;
        final Recipe recipe = new Recipe();
        final RecipeRating existing = RecipeRating.builder()
                .id(1L)
                .recipe(recipe)
                .userId(userId)
                .rating(0.8)
                .build();
        final RecipeResponse response = new RecipeResponse();

        when(this.ratingRepository.findByRecipeIdAndUserId(recipeId, userId)).thenReturn(Optional.of(existing));
        when(this.recipeApiService.getRecipe(recipeId)).thenReturn(response);

        final RecipeResponse result = this.ratingService.changeRating(recipeId, newRating, userId);

        assertThat(existing.getRating()).isEqualTo(newRating);
        verify(this.ratingRepository).save(existing);
        assertThat(this.entityManager.isFlushed()).isTrue();
        assertThat(this.entityManager.isRefreshed()).isTrue();
        verify(this.recipeApiService).getRecipe(recipeId);
        assertThat(result).isSameAs(response);
    }

    @Test
    @DisplayName("changeRating should throw when out of bounds")
    void changeRating_invalidRating_throws() {
        assertThatThrownBy(() -> this.ratingService.changeRating(5L, 1.5, "user5"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Rating must be between 0 and 1");

        assertThatThrownBy(() -> this.ratingService.changeRating(5L, -0.2, "user5"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Rating must be between 0 and 1");
    }

    @Test
    @DisplayName("changeRating should throw when no existing rating")
    void changeRating_noExisting_throws() {
        final Long recipeId = 6L;
        final String userId = "user6";

        when(this.ratingRepository.findByRecipeIdAndUserId(recipeId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.ratingService.changeRating(recipeId, 0.6, userId))
                .isInstanceOf(NoSuchElementException.class);
    }
}