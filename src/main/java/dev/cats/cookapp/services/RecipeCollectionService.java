package dev.cats.cookapp.services;

import dev.cats.cookapp.dtos.request.CollectionRequest;
import dev.cats.cookapp.dtos.response.CollectionListPreviewResponse;
import dev.cats.cookapp.dtos.response.collection.CollectionListResponse;
import dev.cats.cookapp.dtos.response.collection.FullCollectionResponse;

public interface RecipeCollectionService {
    CollectionListResponse getCollections(String userId);
    CollectionListPreviewResponse getCollectionsPreview(String userId);
    FullCollectionResponse getCollectionById(String userId, Long collectionId);
    FullCollectionResponse addCollection(String userId, CollectionRequest request);
    FullCollectionResponse updateCollection(String userId, CollectionRequest request);
    void deleteCollection(String userId, Long collectionId);
    FullCollectionResponse addRecipeToCollection(String userId, Long collectionId, Long recipeId);
    FullCollectionResponse removeRecipeFromCollection(String userId, Long collectionId, Long recipeId);
}
