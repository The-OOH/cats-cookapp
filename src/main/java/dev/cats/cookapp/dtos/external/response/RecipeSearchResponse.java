package dev.cats.cookapp.dtos.external.response;

import dev.cats.cookapp.dtos.response.PageResponse;
import dev.cats.cookapp.dtos.response.recipe.RecipeResponse;

import java.util.List;

public record RecipeSearchResponse(Boolean success, List<RecipeResponse> data, PageResponse.Meta meta) {

}

