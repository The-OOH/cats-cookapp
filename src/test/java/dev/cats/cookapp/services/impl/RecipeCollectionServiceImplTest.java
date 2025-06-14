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
        final String userId = "user1";
        when(this.collectionRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());
        final CollectionListResponse expected = new CollectionListResponse();
        when(this.collectionsMapper.toListResponse(Collections.emptyList())).thenReturn(expected);

        final CollectionListResponse actual = this.service.getCollections(userId);

        verify(this.collectionRepository).findAllByUserId(userId);
        verify(this.collectionsMapper).toListResponse(Collections.emptyList());
        assertThat(actual).isSameAs(expected);
    }

    @Test
    @DisplayName("getCollectionById throws when not found")
    void getCollectionById_notFound_throws() {
        final String userId = "userA";
        final Long collId = 10L;
        when(this.collectionRepository.findByUserIdAndId(userId, collId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.service.getCollectionById(userId, collId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Collection not found");
    }

    @Test
    @DisplayName("getCollectionById returns mapper response when found")
    void getCollectionById_found_returnsMapperResponse() {
        final String userId = "userB";
        final Long collId = 11L;
        final RecipesCollection coll = new RecipesCollection();
        when(this.collectionRepository.findByUserIdAndId(userId, collId)).thenReturn(Optional.of(coll));
        final FullCollectionResponse expected = new FullCollectionResponse();
        when(this.collectionsMapper.toFullCollectionResponse(coll)).thenReturn(expected);

        final FullCollectionResponse actual = this.service.getCollectionById(userId, collId);

        verify(this.collectionRepository).findByUserIdAndId(userId, collId);
        verify(this.collectionsMapper).toFullCollectionResponse(coll);
        assertThat(actual).isSameAs(expected);
    }

    @Test
    @DisplayName("addCollection throws when name missing")
    void addCollection_missingName_throws() {
        final CollectionRequest req = new CollectionRequest();
        req.setName("");

        assertThatThrownBy(() -> this.service.addCollection("userX", req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Collection name must be provided");
    }

    @Test
    @DisplayName("addCollection throws when recipe IDs invalid")
    void addCollection_invalidRecipeIds_throws() {
        final CollectionRequest req = new CollectionRequest();
        req.setName("My Coll");
        req.setRecipeIds(List.of(1L, 2L));
        when(this.recipeRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(new Recipe()));

        assertThatThrownBy(() -> this.service.addCollection("userY", req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("One or more recipeIds are invalid");
    }

    @Test
    @DisplayName("addCollection persists and returns mapper response")
    void addCollection_valid_savesAndReturns() {
        final CollectionRequest req = new CollectionRequest();
        req.setName("Coll");
        req.setDescription("Desc");
        req.setRecipeIds(List.of(3L));
        final Recipe r = new Recipe(); r.setId(3L);
        when(this.recipeRepository.findAllById(List.of(3L))).thenReturn(List.of(r));
        final RecipesCollection toSave = new RecipesCollection();
        final FullCollectionResponse expected = new FullCollectionResponse();
        when(this.collectionRepository.save(any(RecipesCollection.class))).thenReturn(toSave);
        when(this.collectionsMapper.toFullCollectionResponse(toSave)).thenReturn(expected);

        final FullCollectionResponse actual = this.service.addCollection("userZ", req);

        final ArgumentCaptor<RecipesCollection> captor = ArgumentCaptor.forClass(RecipesCollection.class);
        verify(this.collectionRepository).save(captor.capture());
        final RecipesCollection saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo("Coll");
        assertThat(saved.getDescription()).isEqualTo("Desc");
        assertThat(saved.getUserId()).isEqualTo("userZ");
        verify(this.collectionsMapper).toFullCollectionResponse(toSave);
        assertThat(actual).isSameAs(expected);
    }

    @Test
    @DisplayName("updateCollection throws when ID missing")
    void updateCollection_missingId_throws() {
        final CollectionRequest req = new CollectionRequest();
        req.setId(null);

        assertThatThrownBy(() -> this.service.updateCollection("u1", req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Collection ID must be set");
    }

    @Test
    @DisplayName("updateCollection throws when not found")
    void updateCollection_notFound_throws() {
        final CollectionRequest req = new CollectionRequest();
        req.setId(5L);
        when(this.collectionRepository.findByUserIdAndId("u2", 5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.service.updateCollection("u2", req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Collection not found");
    }

    @Test
    @DisplayName("updateCollection throws when recipe IDs invalid")
    void updateCollection_invalidRecipeIds_throws() {
        final CollectionRequest req = new CollectionRequest();
        req.setId(6L);
        req.setName("N");
        req.setDescription("D");
        req.setRecipeIds(List.of(7L));
        final RecipesCollection existing = new RecipesCollection();
        when(this.collectionRepository.findByUserIdAndId("u3", 6L)).thenReturn(Optional.of(existing));
        when(this.recipeRepository.findAllById(List.of(7L))).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> this.service.updateCollection("u3", req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("One or more recipeIds are invalid");
    }

    @Test
    @DisplayName("updateCollection persists and returns mapper response")
    void updateCollection_valid_savesAndReturns() {
        final CollectionRequest req = new CollectionRequest();
        req.setId(8L);
        req.setName("NM");
        req.setDescription("DM");
        req.setRecipeIds(List.of(9L));
        final RecipesCollection existing = new RecipesCollection(); existing.setUserId("u4"); existing.setId(8L);
        final Recipe r = new Recipe(); r.setId(9L);
        when(this.collectionRepository.findByUserIdAndId("u4", 8L)).thenReturn(Optional.of(existing));
        when(this.recipeRepository.findAllById(List.of(9L))).thenReturn(List.of(r));
        final RecipesCollection saved = new RecipesCollection();
        final FullCollectionResponse expected = new FullCollectionResponse();
        when(this.collectionRepository.save(existing)).thenReturn(saved);
        when(this.collectionsMapper.toFullCollectionResponse(saved)).thenReturn(expected);

        final FullCollectionResponse actual = this.service.updateCollection("u4", req);

        verify(this.collectionRepository).save(existing);
        verify(this.collectionsMapper).toFullCollectionResponse(saved);
        assertThat(actual).isSameAs(expected);
    }

    @Test
    @DisplayName("deleteCollection throws when not found")
    void deleteCollection_notFound_throws() {
        when(this.collectionRepository.findByUserIdAndId("u5", 10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.service.deleteCollection("u5", 10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Collection not found");
    }

    @Test
    @DisplayName("deleteCollection delegates when found")
    void deleteCollection_found_delegates() {
        final RecipesCollection coll = new RecipesCollection();
        when(this.collectionRepository.findByUserIdAndId("u6", 11L)).thenReturn(Optional.of(coll));

        this.service.deleteCollection("u6", 11L);

        verify(this.collectionRepository).delete(coll);
    }

    @Test
    @DisplayName("addRecipeToCollection throws when collection not found")
    void addRecipeToCollection_noCollection_throws() {
        when(this.collectionRepository.findByUserIdAndId("u7", 12L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.service.addRecipeToCollection("u7", 12L, 20L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Collection not found");
    }

    @Test
    @DisplayName("addRecipeToCollection throws when recipe not found")
    void addRecipeToCollection_noRecipe_throws() {
        final RecipesCollection coll = new RecipesCollection();
        when(this.collectionRepository.findByUserIdAndId("u8", 13L)).thenReturn(Optional.of(coll));
        when(this.recipeRepository.findById(30L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.service.addRecipeToCollection("u8", 13L, 30L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Recipe not found");
    }

    @Test
    @DisplayName("addRecipeToCollection persists and returns mapper response")
    void addRecipeToCollection_valid_savesAndReturns() {
        final RecipesCollection coll = new RecipesCollection(); coll.setUserId("u9"); coll.setId(14L);
        final Recipe r = new Recipe(); r.setId(40L);
        when(this.collectionRepository.findByUserIdAndId("u9", 14L)).thenReturn(Optional.of(coll));
        when(this.recipeRepository.findById(40L)).thenReturn(Optional.of(r));
        final RecipesCollection saved = new RecipesCollection();
        final FullCollectionResponse expected = new FullCollectionResponse();
        when(this.collectionRepository.save(coll)).thenReturn(saved);
        when(this.collectionsMapper.toFullCollectionResponse(saved)).thenReturn(expected);

        final FullCollectionResponse actual = this.service.addRecipeToCollection("u9", 14L, 40L);

        verify(this.collectionRepository).save(coll);
        verify(this.collectionsMapper).toFullCollectionResponse(saved);
        assertThat(actual).isSameAs(expected);
    }

    @Test
    @DisplayName("removeRecipeFromCollection throws when collection not found")
    void removeRecipeFromCollection_noCollection_throws() {
        when(this.collectionRepository.findByUserIdAndId("u11", 15L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.service.removeRecipeFromCollection("u11", 15L, 50L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Collection not found");
    }

    @Test
    @DisplayName("removeRecipeFromCollection persists and returns mapper response")
    void removeRecipeFromCollection_valid_savesAndReturns() {
        final RecipesCollection coll = new RecipesCollection(); coll.setUserId("u10"); coll.setId(16L);
        final Recipe r = new Recipe(); r.setId(50L);
        coll.addRecipe(r);
        when(this.collectionRepository.findByUserIdAndId("u10", 16L)).thenReturn(Optional.of(coll));
        when(this.recipeRepository.getReferenceById(50L)).thenReturn(r);
        final RecipesCollection saved = new RecipesCollection();
        final FullCollectionResponse expected = new FullCollectionResponse();
        when(this.collectionRepository.save(coll)).thenReturn(saved);
        when(this.collectionsMapper.toFullCollectionResponse(saved)).thenReturn(expected);

        final FullCollectionResponse actual = this.service.removeRecipeFromCollection("u10", 16L, 50L);

        verify(this.collectionRepository).save(coll);
        verify(this.collectionsMapper).toFullCollectionResponse(saved);
        assertThat(actual).isSameAs(expected);
    }
}
