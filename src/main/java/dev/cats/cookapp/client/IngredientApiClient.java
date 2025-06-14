package dev.cats.cookapp.client;

import dev.cats.cookapp.dtos.external.request.IngredientSearchRequest;
import dev.cats.cookapp.dtos.external.response.IngredientSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientApiClient {
    private final WebClient client;

    public Mono<List<IngredientSearchResponse.Result>> searchIngredients(final List<String> queries, final String userId) {
        final IngredientSearchRequest body = new IngredientSearchRequest(
                queries, 1, 3, true, 0.5);

        return this.client.post()
                .uri("/ingredients/search/multiple")
                .header("x-user-id", userId)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(IngredientSearchResponse.class)
                .map(r -> r.data().results());
    }
}
