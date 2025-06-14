package dev.cats.cookapp.dtos.external.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RecipeSearchRequest(
        int page,
        int limit,
        String searchQuery,
        TimeRange cookTime,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<String> difficulty,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<String> dishTypes,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<String> diets,
        IngredientIds ingredients) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record TimeRange(Integer min, Integer max) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record IngredientIds(List<Integer> includeIds, List<Integer> excludeIds) {
    }
}