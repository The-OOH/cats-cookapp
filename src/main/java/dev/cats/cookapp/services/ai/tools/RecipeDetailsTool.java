package dev.cats.cookapp.services.ai.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cats.cookapp.client.IngredientApiClient;
import dev.cats.cookapp.services.RecipeAPIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecipeDetailsTool {
    private final ObjectMapper mapper;
    private final RecipeAPIService recipeAPIService;

    @Tool(
            name = "recipe_details",
            description = """
                    Call in cases when you need to get recipe details recipe id to tell user more about it.
                    """)
    public String getRecipeDetails(
            @ToolParam(
                    description = "Recipe identifier. You can get it from 'search_recipes' tool",
                    required = true
            ) final
            Long recipeId)
            throws Exception {
        final var recipe = this.recipeAPIService.getRecipe(recipeId);
        RecipeDetailsTool.log.info("Get recipe details: {}", recipe);
        return this.mapper.writeValueAsString(recipe) + "\n" + "Based on this result provide user message with type 'RECIPE_DETAILS'";
    }
}
