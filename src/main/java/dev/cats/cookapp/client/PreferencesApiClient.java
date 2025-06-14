package dev.cats.cookapp.client;

import dev.cats.cookapp.dtos.external.response.preferences.PreferencesPayload;
import dev.cats.cookapp.dtos.external.response.preferences.PreferencesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PreferencesApiClient {
    private final WebClient client;
    private final CacheManager cacheManager;

    @Cacheable(cacheNames = "preferences", key = "#userId")
    public Mono<PreferencesPayload> getPreferences(final String userId) {

        return this.client.get()
                .uri("/preferences")
                .header("x-user-id", userId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PreferencesResponse>() {
                })
                .map(PreferencesResponse::getData);
    }
}
