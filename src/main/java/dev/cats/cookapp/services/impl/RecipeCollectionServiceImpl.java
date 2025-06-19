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
    public CollectionListResponse getCollections(final String userId) {
        final List<RecipesCollection> collections = getCollectionList(userId);

        final List<CollectionResponse> dtos = collections.stream()
                .map(coll -> {
                    final List<RecipeCollectionResponse> top3 = coll.getRecipes().stream()
                            .sorted(Comparator.comparing(Recipe::getFinalRating,
                                    Comparator.nullsLast(Comparator.reverseOrder())))
                            .limit(3)
                            .map(this.recipeMapper::toCollectionResponse)
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

        return this.collectionsMapper.toListResponse(dtos);
    }


    @Override
    public CollectionListPreviewResponse getCollectionsPreview(final String userId) {
        final List<RecipesCollection> collections = getCollectionList(userId);
        final List<CollectionListPreviewResponse.CollectionPreviewResponse> dtos = collections.stream()
                .map(coll -> new CollectionListPreviewResponse.CollectionPreviewResponse(
                        coll.getId(),
                        coll.getName()
                ))
                .toList();
        return new CollectionListPreviewResponse(dtos);
    }

    @Override
    public FullCollectionResponse getCollectionById(final String userId, final Long collectionId) {
        final RecipesCollection coll = this.collectionRepository.findByUserIdAndId(userId, collectionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Collection not found for user=" + userId + " id=" + collectionId
                ));
        return this.collectionsMapper.toFullCollectionResponse(coll);
    }

    @Override
    public FullCollectionResponse addCollection(final String userId, final CollectionRequest req) {
        Assert.hasText(req.getName(), "Collection name must be provided");

        final RecipesCollection coll = this.toEntity(req, userId);
        if (null != req.getRecipeIds() && !req.getRecipeIds().isEmpty()) {
            final List<Recipe> recipes = this.recipeRepository.findAllById(req.getRecipeIds());
            Assert.isTrue(
                    recipes.size() == req.getRecipeIds().size(),
                    "One or more recipeIds are invalid"
            );
            recipes.forEach(coll::addRecipe);
        }

        final RecipesCollection saved = this.collectionRepository.save(coll);
        return this.collectionsMapper.toFullCollectionResponse(saved);
    }

    @Override
    public FullCollectionResponse updateCollection(final String userId, final CollectionRequest req) {
        Assert.notNull(req.getId(), "Collection ID must be set for update");
        final RecipesCollection coll = this.collectionRepository.findByUserIdAndId(userId, req.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Collection not found for user=" + userId + " id=" + req.getId()
                ));

        coll.setName(req.getName());
        coll.setDescription(req.getDescription());

        if (null != req.getRecipeIds()) {
            coll.getCollectionRecipes().clear();
            final List<Recipe> recipes = this.recipeRepository.findAllById(req.getRecipeIds());
            Assert.isTrue(
                    recipes.size() == req.getRecipeIds().size(),
                    "One or more recipeIds are invalid"
            );
            recipes.forEach(coll::addRecipe);
        }

        final RecipesCollection saved = this.collectionRepository.save(coll);
        return this.collectionsMapper.toFullCollectionResponse(saved);
    }

    @Override
    public void deleteCollection(final String userId, final Long collectionId) {
        final RecipesCollection coll = this.collectionRepository.findByUserIdAndId(userId, collectionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Collection not found for user=" + userId + " id=" + collectionId
                ));
        this.collectionRepository.delete(coll);
    }

    @Override
    public FullCollectionResponse addRecipeToCollection(final String userId, final Long collectionId, final Long recipeId) {
        final RecipesCollection coll = this.collectionRepository.findByUserIdAndId(userId, collectionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Collection not found for user=" + userId + " id=" + collectionId
                ));
        final Recipe recipe = this.recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found id=" + recipeId));
        coll.addRecipe(recipe);
        final RecipesCollection saved = this.collectionRepository.save(coll);
        return this.collectionsMapper.toFullCollectionResponse(saved);
    }

    @Override
    public FullCollectionResponse removeRecipeFromCollection(final String userId, final Long collectionId, final Long recipeId) {
        final RecipesCollection coll = this.collectionRepository.findByUserIdAndId(userId, collectionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Collection not found for user=" + userId + " id=" + collectionId
                ));
        final Recipe recipe = this.recipeRepository.getReferenceById(recipeId);
        coll.removeRecipe(recipe);
        final RecipesCollection saved = this.collectionRepository.save(coll);
        return this.collectionsMapper.toFullCollectionResponse(saved);
    }

    private RecipesCollection toEntity(final CollectionRequest req, final String userId) {
        final RecipesCollection coll = new RecipesCollection();
        coll.setUserId(userId);
        coll.setName(req.getName());
        coll.setDescription(req.getDescription());
        coll.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return coll;
    }

    private List<RecipesCollection> getCollectionList(final String userId) {
        final List<RecipesCollection> collections = this.collectionRepository.findAllByUserId(userId);

        if (collections.isEmpty()) {
            var recipeCollection = new RecipesCollection();
            recipeCollection.setUserId(userId);
            recipeCollection.setName("My");
            recipeCollection.setDescription("My default collection");
            collectionRepository.save(recipeCollection);
            return List.of(recipeCollection);
        }
        return collections;
    }
}
