package dev.cats.cookapp.dtos.response;

import java.util.List;

public record CollectionListPreviewResponse(
        List<CollectionPreviewResponse> collections) {
    public record CollectionPreviewResponse(
            Long id,
            String name
    ) {
    }
}