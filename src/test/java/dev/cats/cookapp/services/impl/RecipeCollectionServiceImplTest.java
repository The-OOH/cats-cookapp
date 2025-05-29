package dev.cats.cookapp.services.impl;

import dev.cats.cookapp.dtos.request.CollectionRequest;
import dev.cats.cookapp.dtos.response.collection.CollectionListResponse;
import dev.cats.cookapp.dtos.response.collection.FullCollectionResponse;
import dev.cats.cookapp.mappers.CollectionsMapper;
import dev.cats.cookapp.mappers.RecipeMapper;
import dev.cats.cookapp.models.collection.RecipesCollection;
import dev.cats.cookapp.models.recipe.Recipe;
import dev.cats.cookapp.repositories.RecipeCollectionRepository;
import dev.cats.cookapp.repositories.RecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeCollectionServiceImplTest {

    @Mock
    private RecipeCollectionRepository collectionRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private CollectionsMapper collectionsMapper;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeCollectionServiceImpl service;

    @Test
    @DisplayName("getCollections returns mapper response for empty list")
    void getCollections_empty_returnsMapperResponse() {
        String userId = "user1";
        when(collectionRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());
        CollectionListResponse expected = new CollectionListResponse();
        when(collectionsMapper.toListResponse(Collections.emptyList())).thenReturn(expected);

        CollectionListResponse actual = service.getCollections(userId);

        verify(collectionRepository).findAllByUserId(userId);
        verify(collectionsMapper).toListResponse(Collections.emptyList());
        assertThat(actual).isSameAs(expected);
    }

    @Test
    @DisplayName("getCollectionById throws when not found")
    void getCollectionById_notFound_throws() {
        String userId = "userA";
        Long collId = 10L;
        when(collectionRepository.findByUserIdAndId(userId, collId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCollectionById(userId, collId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Collection not found");
    }

    @Test
    @DisplayName("getCollectionById returns mapper response when found")
    void getCollectionById_found_returnsMapperResponse() {
        String userId = "userB";
        Long collId = 11L;
        RecipesCollection coll = new RecipesCollection();
        when(collectionRepository.findByUserIdAndId(userId, collId)).thenReturn(Optional.of(coll));
        FullCollectionResponse expected = new FullCollectionResponse();
        when(collectionsMapper.toFullCollectionResponse(coll)).thenReturn(expected);

        FullCollectionResponse actual = service.getCollectionById(userId, collId);

        verify(collectionRepository).findByUserIdAndId(userId, collId);
        verify(collectionsMapper).toFullCollectionResponse(coll);
        assertThat(actual).isSameAs(expected);
    }

    @Test
    @DisplayName("addCollection throws when name missing")
    void addCollection_missingName_throws() {
        CollectionRequest req = new CollectionRequest();
        req.setName("");

        assertThatThrownBy(() -> service.addCollection("userX", req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Collection name must be provided");
    }

    @Test
    @DisplayName("addCollection throws when recipe IDs invalid")
    void addCollection_invalidRecipeIds_throws() {
        CollectionRequest req = new CollectionRequest();
        req.setName("My Coll");
        req.setRecipeIds(List.of(1L, 2L));
        when(recipeRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(new Recipe()));

        assertThatThrownBy(() -> service.addCollection("userY", req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("One or more recipeIds are invalid");
    }

    @Test
    @DisplayName("addCollection persists and returns mapper response")
    void addCollection_valid_savesAndReturns() {
        CollectionRequest req = new CollectionRequest();
        req.setName("Coll");
        req.setDescription("Desc");
        req.setRecipeIds(List.of(3L));
        Recipe r = new Recipe(); r.setId(3L);
        when(recipeRepository.findAllById(List.of(3L))).thenReturn(List.of(r));
        RecipesCollection toSave = new RecipesCollection();
        FullCollectionResponse expected = new FullCollectionResponse();
        when(collectionRepository.save(any(RecipesCollection.class))).thenReturn(toSave);
        when(collectionsMapper.toFullCollectionResponse(toSave)).thenReturn(expected);

        FullCollectionResponse actual = service.addCollection("userZ", req);

        ArgumentCaptor<RecipesCollection> captor = ArgumentCaptor.forClass(RecipesCollection.class);
        verify(collectionRepository).save(captor.capture());
        RecipesCollection saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo("Coll");
        assertThat(saved.getDescription()).isEqualTo("Desc");
        assertThat(saved.getUserId()).isEqualTo("userZ");
        verify(collectionsMapper).toFullCollectionResponse(toSave);
        assertThat(actual).isSameAs(expected);
    }

    @Test
    @DisplayName("updateCollection throws when ID missing")
    void updateCollection_missingId_throws() {
        CollectionRequest req = new CollectionRequest();
        req.setId(null);

        assertThatThrownBy(() -> service.updateCollection("u1", req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Collection ID must be set");
    }

    @Test
    @DisplayName("updateCollection throws when not found")
    void updateCollection_notFound_throws() {
        CollectionRequest req = new CollectionRequest();
        req.setId(5L);
        when(collectionRepository.findByUserIdAndId("u2", 5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateCollection("u2", req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Collection not found");
    }

    @Test
    @DisplayName("updateCollection throws when recipe IDs invalid")
    void updateCollection_invalidRecipeIds_throws() {
        CollectionRequest req = new CollectionRequest();
        req.setId(6L);
        req.setName("N");
        req.setDescription("D");
        req.setRecipeIds(List.of(7L));
        RecipesCollection existing = new RecipesCollection();
        when(collectionRepository.findByUserIdAndId("u3", 6L)).thenReturn(Optional.of(existing));
        when(recipeRepository.findAllById(List.of(7L))).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> service.updateCollection("u3", req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("One or more recipeIds are invalid");
    }

    @Test
    @DisplayName("updateCollection persists and returns mapper response")
    void updateCollection_valid_savesAndReturns() {
        CollectionRequest req = new CollectionRequest();
        req.setId(8L);
        req.setName("NM");
        req.setDescription("DM");
        req.setRecipeIds(List.of(9L));
        RecipesCollection existing = new RecipesCollection(); existing.setUserId("u4"); existing.setId(8L);
        Recipe r = new Recipe(); r.setId(9L);
        when(collectionRepository.findByUserIdAndId("u4", 8L)).thenReturn(Optional.of(existing));
        when(recipeRepository.findAllById(List.of(9L))).thenReturn(List.of(r));
        RecipesCollection saved = new RecipesCollection();
        FullCollectionResponse expected = new FullCollectionResponse();
        when(collectionRepository.save(existing)).thenReturn(saved);
        when(collectionsMapper.toFullCollectionResponse(saved)).thenReturn(expected);

        FullCollectionResponse actual = service.updateCollection("u4", req);

        verify(collectionRepository).save(existing);
        verify(collectionsMapper).toFullCollectionResponse(saved);
        assertThat(actual).isSameAs(expected);
    }

    @Test
    @DisplayName("deleteCollection throws when not found")
    void deleteCollection_notFound_throws() {
        when(collectionRepository.findByUserIdAndId("u5", 10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteCollection("u5", 10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Collection not found");
    }

    @Test
    @DisplayName("deleteCollection delegates when found")
    void deleteCollection_found_delegates() {
        RecipesCollection coll = new RecipesCollection();
        when(collectionRepository.findByUserIdAndId("u6", 11L)).thenReturn(Optional.of(coll));

        service.deleteCollection("u6", 11L);

        verify(collectionRepository).delete(coll);
    }

    @Test
    @DisplayName("addRecipeToCollection throws when collection not found")
    void addRecipeToCollection_noCollection_throws() {
        when(collectionRepository.findByUserIdAndId("u7", 12L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addRecipeToCollection("u7", 12L, 20L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Collection not found");
    }

    @Test
    @DisplayName("addRecipeToCollection throws when recipe not found")
    void addRecipeToCollection_noRecipe_throws() {
        RecipesCollection coll = new RecipesCollection();
        when(collectionRepository.findByUserIdAndId("u8", 13L)).thenReturn(Optional.of(coll));
        when(recipeRepository.findById(30L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addRecipeToCollection("u8", 13L, 30L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Recipe not found");
    }

    @Test
    @DisplayName("addRecipeToCollection persists and returns mapper response")
    void addRecipeToCollection_valid_savesAndReturns() {
        RecipesCollection coll = new RecipesCollection(); coll.setUserId("u9"); coll.setId(14L);
        Recipe r = new Recipe(); r.setId(40L);
        when(collectionRepository.findByUserIdAndId("u9", 14L)).thenReturn(Optional.of(coll));
        when(recipeRepository.findById(40L)).thenReturn(Optional.of(r));
        RecipesCollection saved = new RecipesCollection();
        FullCollectionResponse expected = new FullCollectionResponse();
        when(collectionRepository.save(coll)).thenReturn(saved);
        when(collectionsMapper.toFullCollectionResponse(saved)).thenReturn(expected);

        FullCollectionResponse actual = service.addRecipeToCollection("u9", 14L, 40L);

        verify(collectionRepository).save(coll);
        verify(collectionsMapper).toFullCollectionResponse(saved);
        assertThat(actual).isSameAs(expected);
    }

    @Test
    @DisplayName("removeRecipeFromCollection throws when collection not found")
    void removeRecipeFromCollection_noCollection_throws() {
        when(collectionRepository.findByUserIdAndId("u11", 15L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.removeRecipeFromCollection("u11", 15L, 50L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Collection not found");
    }

    @Test
    @DisplayName("removeRecipeFromCollection persists and returns mapper response")
    void removeRecipeFromCollection_valid_savesAndReturns() {
        RecipesCollection coll = new RecipesCollection(); coll.setUserId("u10"); coll.setId(16L);
        Recipe r = new Recipe(); r.setId(50L);
        coll.addRecipe(r);
        when(collectionRepository.findByUserIdAndId("u10", 16L)).thenReturn(Optional.of(coll));
        when(recipeRepository.getReferenceById(50L)).thenReturn(r);
        RecipesCollection saved = new RecipesCollection();
        FullCollectionResponse expected = new FullCollectionResponse();
        when(collectionRepository.save(coll)).thenReturn(saved);
        when(collectionsMapper.toFullCollectionResponse(saved)).thenReturn(expected);

        FullCollectionResponse actual = service.removeRecipeFromCollection("u10", 16L, 50L);

        verify(collectionRepository).save(coll);
        verify(collectionsMapper).toFullCollectionResponse(saved);
        assertThat(actual).isSameAs(expected);
    }
}
