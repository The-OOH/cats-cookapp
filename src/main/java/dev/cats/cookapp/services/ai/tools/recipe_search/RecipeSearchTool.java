package dev.cats.cookapp.services.ai.tools.recipe_search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cats.cookapp.client.RecipeApiClient;
import dev.cats.cookapp.dtos.external.request.RecipeSearchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecipeSearchTool {
    private final RecipeApiClient api;
    private final ObjectMapper mapper;

    public String searchRecipes(final String searchQuery,
                                final List<Integer> excludeIngredientIds,
                                final List<String> difficulties,
                                final Integer maxCookingTime,
                                final Integer minCookingTime,
                                final ToolContext ctx) {
        final String userId = ctx.getContext().get("userId").toString();

        final var rq = new RecipeSearchRequest(
                1,
                3,
                null == searchQuery ? "" : searchQuery,
                new RecipeSearchRequest.TimeRange(null == minCookingTime ? 1 : minCookingTime, null == maxCookingTime ? 1 : maxCookingTime),
                difficulties,
                null,
                null,
                new RecipeSearchRequest.IngredientIds(null, excludeIngredientIds)
        );

        try {
            return this.api.search(rq, userId)
                    .timeout(Duration.ofSeconds(8))
                    .map(resp -> {
                        try {
                            return this.mapper.writeValueAsString(resp);
                        } catch (final JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .defaultIfEmpty("{\"message\":\"No recipes found\"}")   // never null
                    .block();
        } catch (final Exception ex) {
            RecipeSearchTool.log.error("Recipe search error", ex);
            return "Sorry, I couldn't find recipes with those filters. Please refine your request.";
        }
    }

}
