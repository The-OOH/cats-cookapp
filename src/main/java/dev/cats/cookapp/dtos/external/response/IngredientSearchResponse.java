package dev.cats.cookapp.dtos.external.response;

import java.util.List;

public record IngredientSearchResponse(
        boolean success,
        DataBlock data
) {
    public record DataBlock(
            List<Result> results,
            int totalQueries
    ) {
    }

    public record Result(
            String query,
            List<IngredientDto> ingredients,
            Meta meta
    ) {
    }

    public record IngredientDto(
            int id,
            String name,
            double similarity
    ) {
    }

    public record Meta(
            int page,
            int totalPages,
            int totalItems
    ) {
    }
}