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
        private boolean flushed = false;
        private boolean refreshed = false;

        @Override
        public void persist(Object o) {

        }

        @Override
        public <T> T merge(T t) {
            return null;
        }

        @Override
        public void remove(Object o) {

        }

        @Override
        public <T> T find(Class<T> aClass, Object o) {
            return null;
        }

        @Override
        public <T> T find(Class<T> aClass, Object o, Map<String, Object> map) {
            return null;
        }

        @Override
        public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType) {
            return null;
        }

        @Override
        public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType, Map<String, Object> map) {
            return null;
        }

        @Override
        public <T> T getReference(Class<T> aClass, Object o) {
            return null;
        }

        @Override
        public void flush() {
            flushed = true;
        }

        @Override
        public void setFlushMode(FlushModeType flushModeType) {

        }

        @Override
        public FlushModeType getFlushMode() {
            return null;
        }

        @Override
        public void lock(Object o, LockModeType lockModeType) {

        }

        @Override
        public void lock(Object o, LockModeType lockModeType, Map<String, Object> map) {

        }

        @Override
        public void refresh(Object entity) {
            refreshed = true;
        }

        @Override
        public void refresh(Object o, Map<String, Object> map) {

        }

        @Override
        public void refresh(Object o, LockModeType lockModeType) {

        }

        @Override
        public void refresh(Object o, LockModeType lockModeType, Map<String, Object> map) {

        }

        @Override
        public void clear() {

        }

        @Override
        public void detach(Object o) {

        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public LockModeType getLockMode(Object o) {
            return null;
        }

        @Override
        public void setProperty(String s, Object o) {

        }

        @Override
        public Map<String, Object> getProperties() {
            return Map.of();
        }

        @Override
        public Query createQuery(String s) {
            return null;
        }

        @Override
        public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
            return null;
        }

        @Override
        public Query createQuery(CriteriaUpdate criteriaUpdate) {
            return null;
        }

        @Override
        public Query createQuery(CriteriaDelete criteriaDelete) {
            return null;
        }

        @Override
        public <T> TypedQuery<T> createQuery(String s, Class<T> aClass) {
            return null;
        }

        @Override
        public Query createNamedQuery(String s) {
            return null;
        }

        @Override
        public <T> TypedQuery<T> createNamedQuery(String s, Class<T> aClass) {
            return null;
        }

        @Override
        public Query createNativeQuery(String s) {
            return null;
        }

        @Override
        public Query createNativeQuery(String s, Class aClass) {
            return null;
        }

        @Override
        public Query createNativeQuery(String s, String s1) {
            return null;
        }

        @Override
        public StoredProcedureQuery createNamedStoredProcedureQuery(String s) {
            return null;
        }

        @Override
        public StoredProcedureQuery createStoredProcedureQuery(String s) {
            return null;
        }

        @Override
        public StoredProcedureQuery createStoredProcedureQuery(String s, Class... classes) {
            return null;
        }

        @Override
        public StoredProcedureQuery createStoredProcedureQuery(String s, String... strings) {
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
        public <T> T unwrap(Class<T> aClass) {
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
        public <T> EntityGraph<T> createEntityGraph(Class<T> aClass) {
            return null;
        }

        @Override
        public EntityGraph<?> createEntityGraph(String s) {
            return null;
        }

        @Override
        public EntityGraph<?> getEntityGraph(String s) {
            return null;
        }

        @Override
        public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> aClass) {
            return List.of();
        }

        // add getters
        public boolean isFlushed() {
            return flushed;
        }

        public boolean isRefreshed() {
            return refreshed;
        }

    }

    private StubEntityManager entityManager;
    private RatingServiceImpl ratingService;

    @BeforeEach
    void setUp() {
        entityManager = new StubEntityManager();
        ratingService = new RatingServiceImpl(recipeService, recipeApiService, ratingRepository, entityManager);
    }

    @Test
    @DisplayName("rateRecipe should save new rating and return updated recipe response when valid and not previously rated")
    void rateRecipe_validInput_savesRatingAndReturnsResponse() {
        Long recipeId = 1L;
        Double ratingValue = 0.75;
        String userId = "user1";
        Recipe recipe = new Recipe();
        RecipeResponse response = new RecipeResponse();

        when(recipeService.getRecipe(recipeId)).thenReturn(recipe);
        when(ratingRepository.findByRecipeIdAndUserId(recipeId, userId)).thenReturn(Optional.empty());
        when(recipeApiService.getRecipe(recipeId)).thenReturn(response);

        RecipeResponse result = ratingService.rateRecipe(recipeId, ratingValue, userId);

        ArgumentCaptor<RecipeRating> captor = ArgumentCaptor.forClass(RecipeRating.class);
        org.mockito.Mockito.verify(ratingRepository).save(captor.capture());
        RecipeRating saved = captor.getValue();
        assertThat(saved.getRecipe()).isSameAs(recipe);
        assertThat(saved.getRating()).isEqualTo(ratingValue);
        assertThat(saved.getUserId()).isEqualTo(userId);

        assertThat(entityManager.isFlushed()).isTrue();
        assertThat(entityManager.isRefreshed()).isTrue();

        org.mockito.Mockito.verify(recipeApiService).getRecipe(recipeId);
        assertThat(result).isSameAs(response);
    }

    @Test
    @DisplayName("rateRecipe should throw when already rated")
    void rateRecipe_alreadyRated_throws() {
        Long recipeId = 2L;
        String userId = "user2";

        when(recipeService.getRecipe(recipeId)).thenReturn(new Recipe());
        when(ratingRepository.findByRecipeIdAndUserId(recipeId, userId)).thenReturn(Optional.of(new RecipeRating()));

        assertThatThrownBy(() -> ratingService.rateRecipe(recipeId, 0.5, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("You already rated this recipe");
    }

    @Test
    @DisplayName("rateRecipe should throw when rating out of bounds")
    void rateRecipe_invalidRating_throws() {
        Long recipeId = 3L;
        String userId = "user3";

        when(recipeService.getRecipe(recipeId)).thenReturn(new Recipe());
        when(ratingRepository.findByRecipeIdAndUserId(recipeId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ratingService.rateRecipe(recipeId, 1.2, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Rating must be between 0 and 1");

        assertThatThrownBy(() -> ratingService.rateRecipe(recipeId, -0.1, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Rating must be between 0 and 1");
    }

    @Test
    @DisplayName("changeRating should update and return response when valid")
    void changeRating_validInput_updatesAndReturns() {
        Long recipeId = 4L;
        String userId = "user4";
        Double newRating = 0.4;
        Recipe recipe = new Recipe();
        RecipeRating existing = RecipeRating.builder()
                .id(1L)
                .recipe(recipe)
                .userId(userId)
                .rating(0.8)
                .build();
        RecipeResponse response = new RecipeResponse();

        when(ratingRepository.findByRecipeIdAndUserId(recipeId, userId)).thenReturn(Optional.of(existing));
        when(recipeApiService.getRecipe(recipeId)).thenReturn(response);

        RecipeResponse result = ratingService.changeRating(recipeId, newRating, userId);

        assertThat(existing.getRating()).isEqualTo(newRating);
        org.mockito.Mockito.verify(ratingRepository).save(existing);
        assertThat(entityManager.isFlushed()).isTrue();
        assertThat(entityManager.isRefreshed()).isTrue();
        org.mockito.Mockito.verify(recipeApiService).getRecipe(recipeId);
        assertThat(result).isSameAs(response);
    }

    @Test
    @DisplayName("changeRating should throw when out of bounds")
    void changeRating_invalidRating_throws() {
        assertThatThrownBy(() -> ratingService.changeRating(5L, 1.5, "user5"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Rating must be between 0 and 1");

        assertThatThrownBy(() -> ratingService.changeRating(5L, -0.2, "user5"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Rating must be between 0 and 1");
    }

    @Test
    @DisplayName("changeRating should throw when no existing rating")
    void changeRating_noExisting_throws() {
        Long recipeId = 6L;
        String userId = "user6";

        when(ratingRepository.findByRecipeIdAndUserId(recipeId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ratingService.changeRating(recipeId, 0.6, userId))
                .isInstanceOf(NoSuchElementException.class);
    }
}