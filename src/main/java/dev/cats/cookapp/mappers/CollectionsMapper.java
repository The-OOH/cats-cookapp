package dev.cats.cookapp.mappers;

import dev.cats.cookapp.dtos.response.collection.CollectionListResponse;
import dev.cats.cookapp.dtos.response.collection.CollectionResponse;
import dev.cats.cookapp.dtos.response.collection.FullCollectionResponse;
import dev.cats.cookapp.models.collection.RecipesCollection;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = RecipeMapper.class)
public interface CollectionsMapper {
    @Mapping(target = "id",          source = "id")
    @Mapping(target = "userId",      source = "userId")
    @Mapping(target = "name",        source = "name")
    @Mapping(target = "recipeCount", expression = "java(collection.getRecipes().size())")
    CollectionResponse toCollectionResponse(RecipesCollection collection);

    @Named("toFullCollectionResponse")
    @Mapping(target = "id",          source = "id")
    @Mapping(target = "userId",      source = "userId")
    @Mapping(target = "name",        source = "name")
    @Mapping(target = "recipeCount", expression = "java(collection.getRecipes().size())")
    FullCollectionResponse toFullCollectionResponse(RecipesCollection collection);

    default CollectionListResponse toListResponse(List<CollectionResponse> list) {
        CollectionListResponse dto = new CollectionListResponse();
        dto.setCollections(list);
        return dto;
    }

}

