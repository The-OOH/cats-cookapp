package dev.cats.cookapp.client;

import dev.cats.cookapp.dtos.external.request.RecipeSearchRequest;
import dev.cats.cookapp.dtos.external.response.FiltersPayload;
import dev.cats.cookapp.dtos.external.response.RecipeSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RecipeApiClient {
    private final WebClient client;
    private final CacheManager cacheManager;

    @Cacheable(cacheNames = "recipeFilters")
    public Mono<FiltersPayload> getFilters() {
        return this.client.get()
                .uri("/recipes/filters")
                .retrieve()
                .bodyToMono(FiltersPayload.class);
    }

    public Mono<RecipeSearchResponse> search(final RecipeSearchRequest rq, final String userId) {
        return this.client.post()
                .uri("/recipes/search")
                .header("x-user-id", userId)
                .bodyValue(rq)
                .retrieve()
                .bodyToMono(RecipeSearchResponse.class);
    }
}
