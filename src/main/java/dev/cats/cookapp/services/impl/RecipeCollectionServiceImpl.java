package dev.cats.cookapp.services.impl;

import dev.cats.cookapp.dtos.request.CollectionRequest;
import dev.cats.cookapp.dtos.response.CollectionListPreviewResponse;
import dev.cats.cookapp.dtos.response.collection.CollectionListResponse;
import dev.cats.cookapp.dtos.response.collection.CollectionResponse;
import dev.cats.cookapp.dtos.response.collection.FullCollectionResponse;
import dev.cats.cookapp.dtos.response.recipe.RecipeCollectionResponse;
import dev.cats.cookapp.mappers.CollectionsMapper;
import dev.cats.cookapp.mappers.RecipeMapper;
import dev.cats.cookapp.models.recipe.Recipe;
import dev.cats.cookapp.models.collection.RecipesCollection;
import dev.cats.cookapp.repositories.RecipeCollectionRepository;
import dev.cats.cookapp.repositories.RecipeRepository;
import dev.cats.cookapp.services.RecipeCollectionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RecipeCollectionServiceImpl implements RecipeCollectionService {
    RecipeCollectionRepository collectionRepository;
    RecipeRepository recipeRepository;
    CollectionsMapper collectionsMapper;
    RecipeMapper recipeMapper;

    @Override
    public CollectionListResponse getCollections(String userId) {
        List<RecipesCollection> collections = collectionRepository.findAllByUserId(userId);

        List<CollectionResponse> dtos = collections.stream()
                .map(coll -> {
                    List<RecipeCollectionResponse> top3 = coll.getRecipes().stream()
                            .sorted(Comparator.comparing(Recipe::getFinalRating,
                                    Comparator.nullsLast(Comparator.reverseOrder())))
                            .limit(3)
                            .map(recipeMapper::toCollectionResponse)
                            .toList();

                    return new CollectionResponse(
                            coll.getId(),
                            coll.getUserId(),
                            coll.getName(),
                            coll.getRecipes().size(),
                            top3
                    );
                })
                .toList();

        return collectionsMapper.toListResponse(dtos);
    }


    @Override
    public CollectionListPreviewResponse getCollectionsPreview(String userId) {
        List<RecipesCollection> collections = collectionRepository.findAllByUserId(userId);
        List<CollectionListPreviewResponse.CollectionPreviewResponse> dtos = collections.stream()
                .map(coll -> new CollectionListPreviewResponse.CollectionPreviewResponse(
                        coll.getId(),
                        coll.getName()
                ))
                .toList();
        return new CollectionListPreviewResponse(dtos);
    }

    @Override
    public FullCollectionResponse getCollectionById(String userId, Long collectionId) {
        RecipesCollection coll = collectionRepository.findByUserIdAndId(userId, collectionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Collection not found for user=" + userId + " id=" + collectionId
                ));
        return collectionsMapper.toFullCollectionResponse(coll);
    }

    @Override
    public FullCollectionResponse addCollection(String userId, CollectionRequest req) {
        Assert.hasText(req.getName(), "Collection name must be provided");

        RecipesCollection coll = toEntity(req, userId);
        if (req.getRecipeIds() != null && !req.getRecipeIds().isEmpty()) {
            List<Recipe> recipes = recipeRepository.findAllById(req.getRecipeIds());
            Assert.isTrue(
                    recipes.size() == req.getRecipeIds().size(),
                    "One or more recipeIds are invalid"
            );
            recipes.forEach(coll::addRecipe);
        }

        RecipesCollection saved = collectionRepository.save(coll);
        return collectionsMapper.toFullCollectionResponse(saved);
    }

    @Override
    public FullCollectionResponse updateCollection(String userId, CollectionRequest req) {
        Assert.notNull(req.getId(), "Collection ID must be set for update");
        RecipesCollection coll = collectionRepository.findByUserIdAndId(userId, req.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Collection not found for user=" + userId + " id=" + req.getId()
                ));

        coll.setName(req.getName());
        coll.setDescription(req.getDescription());

        if (req.getRecipeIds() != null) {
            coll.getCollectionRecipes().clear();
            List<Recipe> recipes = recipeRepository.findAllById(req.getRecipeIds());
            Assert.isTrue(
                    recipes.size() == req.getRecipeIds().size(),
                    "One or more recipeIds are invalid"
            );
            recipes.forEach(coll::addRecipe);
        }

        RecipesCollection saved = collectionRepository.save(coll);
        return collectionsMapper.toFullCollectionResponse(saved);
    }

    @Override
    public void deleteCollection(String userId, Long collectionId) {
        RecipesCollection coll = collectionRepository.findByUserIdAndId(userId, collectionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Collection not found for user=" + userId + " id=" + collectionId
                ));
        collectionRepository.delete(coll);
    }

    @Override
    public FullCollectionResponse addRecipeToCollection(String userId, Long collectionId, Long recipeId) {
        RecipesCollection coll = collectionRepository.findByUserIdAndId(userId, collectionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Collection not found for user=" + userId + " id=" + collectionId
                ));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found id=" + recipeId));
        coll.addRecipe(recipe);
        RecipesCollection saved = collectionRepository.save(coll);
        return collectionsMapper.toFullCollectionResponse(saved);
    }

    @Override
    public FullCollectionResponse removeRecipeFromCollection(String userId, Long collectionId, Long recipeId) {
        RecipesCollection coll = collectionRepository.findByUserIdAndId(userId, collectionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Collection not found for user=" + userId + " id=" + collectionId
                ));
        Recipe recipe = recipeRepository.getReferenceById(recipeId);
        coll.removeRecipe(recipe);
        RecipesCollection saved = collectionRepository.save(coll);
        return collectionsMapper.toFullCollectionResponse(saved);
    }

    private RecipesCollection toEntity(CollectionRequest req, String userId) {
        RecipesCollection coll = new RecipesCollection();
        coll.setUserId(userId);
        coll.setName(req.getName());
        coll.setDescription(req.getDescription());
        coll.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return coll;
    }
}
