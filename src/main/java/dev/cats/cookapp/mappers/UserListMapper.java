package dev.cats.cookapp.mappers;

import dev.cats.cookapp.dto.request.UserListRequest;
import dev.cats.cookapp.dto.response.UserListResponse;
import dev.cats.cookapp.models.Recipe;
import dev.cats.cookapp.models.UserList;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {RecipeMapper.class})
public interface UserListMapper {
    @Mapping(target = "recipes", source = "recipeIds", qualifiedByName = "recipeIdsToRecipes")
    UserList toEntity(UserListRequest userListRequest);

    @Named("recipeIdsToRecipes")
    default Set<Recipe> recipeIdsToRecipes(Set<Long> recipeIds) {
        return recipeIds.stream().map(recipeId -> {
            Recipe recipe = new Recipe();
            recipe.setId(recipeId);
            return recipe;
        }).collect(Collectors.toSet());
    }

    default Set<Long> recipesToRecipeIds(Set<Recipe> recipes) {
        return recipes.stream().map(Recipe::getId).collect(Collectors.toSet());
    }

    UserListResponse toDto(UserList userList);

}