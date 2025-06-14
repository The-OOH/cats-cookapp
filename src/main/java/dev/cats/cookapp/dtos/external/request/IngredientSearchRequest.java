package dev.cats.cookapp.dtos.external.request;

import java.util.List;

public record IngredientSearchRequest(
        List<String> queries,
        int page,
        int limit,
        boolean useSemanticSearch,
        double semanticThreshold) {}