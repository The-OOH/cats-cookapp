package dev.cats.cookapp.controllers;

import dev.cats.cookapp.dtos.request.CollectionRequest;
import dev.cats.cookapp.dtos.response.CollectionListPreviewResponse;
import dev.cats.cookapp.dtos.response.collection.CollectionListResponse;
import dev.cats.cookapp.dtos.response.collection.FullCollectionResponse;
import dev.cats.cookapp.services.RecipeCollectionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipes/collection")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RecipeCollectionController {
    RecipeCollectionService recipeCollectionService;

    @GetMapping
    public ResponseEntity<CollectionListResponse> getCollections(
            @RequestHeader("x-user-id") String userId
    ) {
        return ResponseEntity.ok(this.recipeCollectionService.getCollections(userId));
    }

    @GetMapping("/preview")
    public ResponseEntity<CollectionListPreviewResponse> getCollectionsPreview(
            @RequestHeader("x-user-id") String userId
    ) {
        return ResponseEntity.ok(this.recipeCollectionService.getCollectionsPreview(userId));
    }

    @PostMapping
    public ResponseEntity<FullCollectionResponse> addCollection(
            @RequestHeader("x-user-id") String userId,
            @Valid @RequestBody CollectionRequest request
    ) {
        var created = this.recipeCollectionService.addCollection(userId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping("/{collectionId}")
    public ResponseEntity<FullCollectionResponse> getCollectionById(
            @RequestHeader("x-user-id") String userId,
            @PathVariable Long collectionId
    ) {
        var resp = this.recipeCollectionService.getCollectionById(userId, collectionId);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{collectionId}")
    public ResponseEntity<FullCollectionResponse> updateCollection(
            @RequestHeader("x-user-id") String userId,
            @PathVariable Long collectionId,
            @Valid @RequestBody CollectionRequest request
    ) {
        request.setId(collectionId);
        final var updated = this.recipeCollectionService.updateCollection(userId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{collectionId}")
    public ResponseEntity<Void> deleteCollection(
            @RequestHeader("x-user-id") final String userId,
            @PathVariable final Long collectionId
    ) {
        this.recipeCollectionService.deleteCollection(userId, collectionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{collectionId}/recipe")
    public ResponseEntity<FullCollectionResponse> addRecipeToCollection(
            @RequestHeader("x-user-id") final String userId,
            @PathVariable final Long collectionId,
            @RequestParam("recipeId") final Long recipeId
    ) {
        final var resp = this.recipeCollectionService.addRecipeToCollection(userId, collectionId, recipeId);
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{collectionId}/recipe/{recipeId}")
    public ResponseEntity<FullCollectionResponse> removeRecipeFromCollection(
            @RequestHeader("x-user-id") final String userId,
            @PathVariable final Long collectionId,
            @PathVariable final Long recipeId
    ) {
        final var resp = this.recipeCollectionService.removeRecipeFromCollection(userId, collectionId, recipeId);
        return ResponseEntity.ok(resp);
    }
}
